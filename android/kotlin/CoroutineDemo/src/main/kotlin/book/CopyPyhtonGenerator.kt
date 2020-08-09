package book

/*
《深入理解kotlin协程》中的仿python generator代码实现
* */


fun main() {

}


interface Generator<T>{
    operator fun iterator() : Iterable<T>
}

interface GeneratorScope<T>{

}

//fun <T> generator(block : suspend GeneratorScope<T>.(T) -> Unit) : (T) -> Generator<T>{
//    return {parameter : T->
//
//    }
//}

