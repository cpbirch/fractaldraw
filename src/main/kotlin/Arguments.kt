class Arguments(args: Array<String>) {
    val argMap = args.fold(Pair(emptyMap<String, List<String>>(), "")) { (map, lastKey), elem ->
        if (elem.startsWith("-")) Pair(map + (elem to emptyList()), elem)
        else if (elem.startsWith("neg")) Pair(map + (lastKey to map.getOrDefault(lastKey, emptyList()) + elem.replace("neg", "-")), lastKey)
        else Pair(map + (lastKey to map.getOrDefault(lastKey, emptyList()) + elem), lastKey)
    }.first

    fun asInt(arg: String, default: Int = 0) = argMap.get(arg)?.get(0)?.toInt() ?: default
    fun asFloat(arg: String, default: Float = 0f) = argMap.get(arg)?.get(0)?.toFloat() ?: default
}