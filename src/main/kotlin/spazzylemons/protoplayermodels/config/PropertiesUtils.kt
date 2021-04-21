package spazzylemons.protoplayermodels.config

import java.util.Properties
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

/**
 * Data for serializing a string into a basic data type.
 */
private data class SerializableType<T : Any>(val clazz: KClass<T>, val converter: (String) -> T, val default: T) {
    val type = clazz.createType()
}

/**
 * A list of types with information on how to deserialize them.
 */
private val serializableTypes = arrayOf(
    // integer types (char excluded as it's ✨special✨
    SerializableType(Byte::class, String::toByte, 0),
    SerializableType(Short::class, String::toShort, 0),
    SerializableType(Int::class, String::toInt, 0),
    SerializableType(Long::class, String::toLong, 0),

    // other numeric types
    SerializableType(Float::class, String::toFloat, 0F),
    SerializableType(Double::class, String::toDouble, 0.0),

    // boolean's pretty important
    SerializableType(Boolean::class, String::toBoolean, false),

    // string, ofc
    SerializableType(String::class, { it }, ""),
)

/**
 * If assertions are enabled, this checks that the class being used meets the assumptions that Any.toProperties() and
 * Properties.load(KClass) are expecting. If assertions are not enabled, this does nothing.
 */
private fun assertUsableClass(clazz: KClass<*>) {
    assert(clazz.isData) { "Class must be data class" }
    assert(clazz.constructors.size == 1) { "Class must only have one constructor" }
    if (Any::class.java.desiredAssertionStatus()) {
        for (parameter in clazz.constructors.first().parameters) {
            val type = parameter.type
            assert(serializableTypes.any { it.type == type }) { "Unsupported type" }
        }
    }
}

/**
 * Convert an object (one such that its class is accepted by assertUsableClass()) into a Properties.
 */
fun Any.toProperties(): Properties {
    // get class
    val clazz = this::class
    // assert that it's usable
    assertUsableClass(clazz)
    // get the primary constructor (assertUsableClass asserts we only have one constructor)
    val constructor = clazz.constructors.first()
    // for each constructor parameter, set the value in a Properties
    val props = Properties()
    constructor.parameters.forEach { parameter ->
        // get parameter name
        val name = parameter.name
        // get the member from the object, then convert to string
        val value = clazz.members.first { it.name == name }.call(this).toString()
        // set the property
        props.setProperty(name, value)
    }
    return props
}

/**
 * Convert a Properties into an object of a class (one that is accepted by assertUsableClass()).
 */
fun <T : Any> Properties.load(clazz: KClass<T>): T {
    // assert that the class is usable
    assertUsableClass(clazz)
    // get the primary constructor (assertUsableClass asserts we only have one constructor)
    val constructor = clazz.constructors.first()
    // convert constructor parameters based on properties
    val args = constructor.parameters.map { parameter ->
        // get the type of the parameter
        val type = parameter.type
        // get the value from the Properties
        val value = getProperty(parameter.name)
        // find deserialization data
        val t = serializableTypes.find { it.type == type }!!
        // convert or get default value
        runCatching { t.converter(value) }.getOrDefault(t.default)
    }.toTypedArray()
    // call constructor with given arguments
    return constructor.call(*args)
}