package com.example.kotlinchange

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

// Kotlin 1.4.0

//IDE 멈춤 현상 또는 메모리 누수를 유발하는 수많은 이슈를 포함하여 60여 개의 성능 이슈를 검토하고 수정.
//=> 프로젝트 작업 시 큰 Kotlin 파일을 처음 열면 이제 훨씬 더 빨리 강조 표시된 내용을 볼 수 있음 (1.5~4배 빨라짐.)


//새로운 IDE 기능
// 코루틴 디버거 -> https://kotlinlang.org/docs/whatsnew14.html#coroutine-debugger

//새로운 컴파일러 단계적 도입 => 유형 추론 알고리즘 기본적으로 활성화됨

// 라이브러리 개선 사항
// kotlinx.coroutines 1.3.9 점진적 발전
// kotlinx.serialization 1.0.0-RC 버전 출시 (직렬화 라이브러리)
// kotlin.datetime 라이브러리 테스트 버전 출시.


// 언어적인 추가 부분.

//SAM (Single Abstract Method) 변환을 Kotlin 에서도 변환 할 수 있어졌다.

fun interface IntPredicate {
    fun accept(i: Int): Boolean
}

val isEven = IntPredicate { it % 2 == 0 }

fun samSample() {
    println("Is 7 even? - ${isEven.accept(7)}")
}

//라이브러리 작성자를 위한 명시적 API 모드

//ex) kotlin {
//    // for strict mode
//    explicitApi()
//    // or
//    explicitApi = ExplicitApiMode.Strict
//
//    // for warning mode
//    explicitApiWarning()
//    // or
//    explicitApi = ExplicitApiMode.Warning
//}


//명명된 인수 및 포지셔널 인수의 혼합.
fun reformat(
    str: String,
    uppercaseFirstLetter: Boolean = true,
    wordSeparator: Char = ' '
) {
    // ...
}

//Function call with a named argument in the middle
//reformat("This is a String!", uppercaseFirstLetter = false , '-')


//후행 쉼표
fun _reformat(
    str: String,
    uppercaseFirstLetter: Boolean = true,
    wordSeparator: Char = ' ', //trailing comma
) {
    // ...
}

val colors = listOf(
    "red",
    "green",
    "blue", //trailing comma
)

//호출 가능한 참조 개선 사항
fun foo(i: Int = 0): String = "$i!"

fun apply(func: () -> String): String = func()

fun functionSample() {
    println(apply(::foo))
}

// 이전
// some new overload
fun applyInt(func: (Int) -> String): String = func(0)


fun foo(f: () -> Unit) {}
fun returnsInt(): Int = 42

fun unitReturningFunctionsExam() {
    foo { returnsInt() } // this was the only way to do it  before 1.4
    foo(::returnsInt) // starting from 1.4, this also works
}

//루프의 when 내에서 break 및 continue 사용
fun testBefore_1_4(xs: List<Int>) {
    LOOP@ for (x in xs) {
        when (x) {
            2 -> continue@LOOP
            17 -> break@LOOP
            else -> println(x)
        }
    }
}

fun testStart_1_4(xs: List<Int>) {
    for (x in xs) {
        when (x) {
            2 -> continue
            17 -> break
            else -> println(x)
        }
    }
}


//1.4.20
// https://blog.jetbrains.com/ko/kotlin/2020/11/kotlin-1-4-20-released/


//invokedynamic 문자열 연결

fun invokeDynamicSample() {

    val a = 1
    val b = 2

    println(a + b)
    println(a.plus(b))


    //-Xstring-concat 옵션 추가.
//    println((a::plus)(b))

}

// 표준 라이브러리 변경 내용.

// java.nio.file.Path용 확장 기능

fun filePathSample() {

    // 1.4.2에서 실험적 제공후 1.5에서 정식 제공.
    val baseDir = Path("/base")

    //list files in a directory
    val kotlinFiles: List<Path> = Path("/home/user").listDirectoryEntries("*.kt")
}

// 내부적으로 new DirectoryStream<Path> 를 호출하고 여기서는 filter 처리하고 있는게 보인다.

//public interface DirectoryStream<T> extends Closeable, Iterable<T> {
//    Iterator<T> iterator();
//
//    @FunctionalInterface
//    public interface Filter<T> {
//        boolean accept(T var1) throws IOException;
//    }
//}

// Kotlin Android Extensions 지원 중단

// Kotlin synthetics 를 대체하는 뷰 바인딩이 생기면서 Kotlin Android Extensions 는 지원 중단. 원한다면 별도 플러그인 제공해줌.

// Parcelable 구현 생성기는 이제 kotlin-parcelize 플러그인에서 사용할 수 있게 되어짐.




