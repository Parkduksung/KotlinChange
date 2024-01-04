package com.example.kotlinchange

import kotlin.reflect.cast

/**
 *
 * Kotlin Version 1.3.70 추가 사항.
 * Link : https://blog.jetbrains.com/kotlin/2020/03/kotlin-1-3-70-released/
 */

fun main() {
    println("Hello, world!!!")
}


//KClass 작업

//KClass 의 일부 유용한 기본 멤버에서 JVM 에 대한 kotlin-reflect 종속 요소가 필요 없어짐.
//이전에는 런타임에서 Kotlin 리플렉션 구현을 제공해야 했지만, 이제 추가 종속 요소 없이 간단한 멤버 사용 가능.
fun kClassExam(){

    val kClass = String::class

    println(kClass.simpleName) // String
    println(kClass.qualifiedName) // kotlin.String

    println(kClass.isInstance("abc")) // true
    println(kClass.isInstance(10)) // false
    println(kClass.cast("abc")) // abc

}

//실험적 주석 이름 변경

// (@Experimental 및 @UseExperimental) => (@OptIn 및 @RequiresOptIn) 변경.
// 컴파일러 인수 -Xuse-experimental => -Xopt-in. 변경
// -Xexperimental => 거의 사용하지 않아 복잡성 증가시키기 때문에 제거.
// (@Experimental 및 @UseExperimental) 는 1.3.70 에서는 여전히 지원되지만 1.4 에서 없어진다.


//양방향 큐 구현 : ArrayDeque
//양방향 큐를 사용하면 상각된 일정한 시간에 큐의 시작과 끝 모두에서 요소를 추가/제거할 수 있음.
fun arrayDequeExam(){
    val deque = ArrayDeque(listOf(1, 2, 3))

    deque.addFirst(0)
    deque.addLast(4)
    println(deque) // [0, 1, 2, 3, 4]

    println(deque.first()) // 0
    println(deque.last()) // 4

    deque.removeFirst()
    deque.removeLast()
    println(deque) // [1, 2, 3]
}
//개념적으로 ArrayDeque의 구현은 java.util.ArrayDeque의 구현과 매우 유사합니다.
//그러나 이는 다른 구현이며 이 클래스가 Kotlin/JVM에서 사용될 때 이 새로운 구현이 사용됩니다.
//이는 다른 컬렉션과 작동 방식이 다릅니다.
//ArrayList를 생성하고 이를 JVM으로 컴파일할 때 java.util.ArrayList 클래스가 내부에서 사용됩니다.
//Collection 인터페이스만 구현하는 Java의 ArrayDeque와 달리 Kotlin의 ArrayDeque는 MutableList를 구현합니다.
//즉, 색인으로 모든 요소에 액세스할 수 있으며 이는 Java의 ArrayDeque에서는 불가능합니다.


//컬렉션 빌더
//또 다른 중요한 새 기능은 컬렉션에 대한 빌더 함수인 buildList, buildSet, buildMap입니다.
//이 빌더 함수를 사용하면 생성 단계에서 가변 컬렉션을 편리하게 조작하고 읽기 전용 컬렉션을 결과로 얻을 수 있습니다.

@OptIn(ExperimentalStdlibApi::class)
fun builderExam() {
    val needsZero = true
    val initial = listOf(2, 6, 41)

    val ints = buildList { // this: MutableList
        if (needsZero) {
            add(0)
        }
        initial.mapTo(this) { it + 1 }
    }
    println(ints) // [0, 3, 7, 42]
}

//reduceOrNull() 와 randomOrNull() 대응 함수 추가.
@OptIn(ExperimentalStdlibApi::class)
fun reduceOrNullAndRandomOrNullExam() {
    val list = listOf(1, 2, 3)
    println(list.randomOrNull()) // 2
    println(list.reduceOrNull { a, b -> a + b }) // 6

    val emptyList = emptyList<Int>()
    println(emptyList.randomOrNull()) // null
    println(emptyList.reduceOrNull { a, b -> a + b }) // null
}
//random() 또는 reduce()를 사용하면 컬렉션이 비어 있을 때 예외가 발생에 대한 대응 함수.


//scan() 함수 추가
@OptIn(ExperimentalStdlibApi::class)
fun scanExam() {
    val ints = (1..4).asSequence()
    println(ints.fold(0) { acc, elem -> acc + elem }) // 10

    val sequence = ints.scan(0) { acc, elem -> acc + elem }
    println(sequence.toList()) // [0, 1, 3, 6, 10]
}
//scan()은 fold() 유사하지만 다름.
//scan()과 fold() 모두 주어진 2진 연산을 값 시퀀스에 적용하지만 scan()은 중간 결과의 전체 시퀀스를 반환하는
//반면 fold()는 최종 결과만 반환한다는 점이 다르다

//scanReduce() 도 같이 볼 것.