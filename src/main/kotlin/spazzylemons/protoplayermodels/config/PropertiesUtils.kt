package spazzylemons.protoplayermodels.config

import java.util.Properties
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KParameter
import kotlin.reflect.full.createType

private val triviallySerializableTypes = arrayOf(
    // integer types (char excluded as it's ✨special✨
    Byte::class,
    Short::class,
    Int::class,
    Long::class,

    // other numeric types
    Float::class,
    Double::class,

    // boolean's pretty important
    Boolean::class,

    // string, ofc
    String::class,
).map(KClassifier::createType).toTypedArray()

private val byteType = triviallySerializableTypes[0]
private val shortType = triviallySerializableTypes[1]
private val intType = triviallySerializableTypes[2]
private val longType = triviallySerializableTypes[3]

private val floatType = triviallySerializableTypes[4]
private val doubleType = triviallySerializableTypes[5]

private val booleanType = triviallySerializableTypes[6]

private val stringType = triviallySerializableTypes[7]

private fun assertUsableClass(clazz: KClass<*>) {
    assert(clazz.isData) { "Class must be data class" }
    assert(clazz.constructors.size == 1) { "Class must only have one constructor" }
    for (parameter in clazz.constructors.first().parameters) {
        val type = parameter.type
        assert(type in triviallySerializableTypes) { "Unsupported type" }
    }
}

fun <T : Any> T.toProperties(): Properties {
    val clazz = this::class
    assertUsableClass(clazz)
    val constructor = clazz.constructors.first()
    // for each constructor parameter, set the value in a Properties
    val props = Properties()
    constructor.parameters.map(KParameter::name).forEach { name ->
        val value = clazz.members.first { it.name == name }.call(this).toString()
        props.setProperty(name, value)
    }
    return props
}

fun <T : Any> Properties.load(clazz: KClass<T>): T {
    assertUsableClass(clazz)
    val constructor = clazz.constructors.first()
    val args = constructor.parameters.map { it.type to getProperty(it.name) }.map { (argClass, value) ->
        when (argClass) {
            byteType -> runCatching { value.toByte() }.getOrDefault(0)
            shortType -> runCatching { value.toShort() }.getOrDefault(0)
            intType -> runCatching { value.toInt() }.getOrDefault(0)
            longType -> runCatching { value.toLong() }.getOrDefault(0L)

            floatType -> runCatching { value.toFloat() }.getOrDefault(0F)
            doubleType -> runCatching { value.toDouble() }.getOrDefault(0.0)

            booleanType -> value.toBoolean()

            stringType -> value ?: ""

            else -> throw IllegalStateException("unreachable")
        }
    }.toTypedArray()
    return constructor.call(*args)
}