package framework.extensionFunctions

fun <T> T.isIn(aList: List<T>): Boolean {
    return aList.contains(this)
}

fun <T> T.isNotIn(aList: List<T>): Boolean {
    return !aList.contains(this)
}