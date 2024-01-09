package com.example.kotlinchange

import android.graphics.Point
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


//1.4.30
// https://tangunsoft.com/tiptech/detail?id=201&cate=2&vendor=25&tag=&keyword=1.4.30

// 언어 기능 테스트 버전

// inline 클래스 베터 버전 승격.
@JvmInline //1.5 부터 정식 지원.
value class Name(private val s: String)

//위와 같이 JVM backends 에서 사용한다.
inline class _Name(private val s: String)


@JvmInline
value class Negative(val x: Int) {

    // 1.4.30 부터는 init 블록을 추가할 수 있음. 클래스가 인스턴스화된 직후에 실행될 코드를 추가할 수 있어졌다.
    init {
        require(x < 0) { }
    }
}

// JVM 레코드 지원
// JDK 16 릴리즈에는 record 라는 새로운 Java 클래스를 코틀린에서 상호 운용성을 유지하기 위해 실험점 도입.
// 최소 JDK 15 이상 필요.
@JvmRecord
data class User(val name: String, val age: Int)


//sealed interface 실험적 지원.
//sealed interface 는 sealed class 와 동일한 제한을 가지지만, 인터페이스로 선언된다는 점이 다르다.
//더 유연한 sealed 된 (제한된) 계층 구조를 구축할 수 있게 도와준다.

sealed interface Polygon

class Rectangle() : Polygon
class Triangle() : Polygon

fun draw(polygon: Polygon) = when(polygon) {
    is Rectangle -> println("Draw Rectangle")
    is Triangle -> println("Draw Triangle")
}

sealed interface Fillable {
    fun fill()
}

sealed interface Polygon2 {
    val vertices : List<Point>
}


// sealed interface 를 사용하여 하나 이상의 실드 슈퍼 클래스에서 클래스를 상속할 수 있다.
class Rectangle2(
    override val vertices: List<Point>
) : Polygon2, Fillable {
    override fun fill() {
        println("Fill Rectangle")
    }
}

//Gradle 프로젝트 개선
// Gradle 구성 캐시 지원 -> 후속 빌드 재사용 -> 빌드 프로세스 가속화.

//대소문자 변환 API 실험적 추가.


//String
val a = "abc".uppercase() // ABC
val b = "AbC".lowercase() // abc
val c = "abc".replaceFirstChar { it.uppercase() } // Abc
val d = "ABC".replaceFirstChar { it.lowercase() } // aBC

//Char
//uppercase(), lowercase() 및 titlecase()



//문자를 숫자로 명확하게 변환하는 확장함수 추가.
// digitToInt(radix : Int) , digitToIntOrNull(radix: Int), digitToChar(radix: Int)



//kotlinx.serialization 1.1.0-RC 버전 출시
//@Serializable 를 사용하여 inline 클래스 직렬화 지원.
// 참고 : https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/value-classes.md

//1.4.30부터 kotlinx.serialization의 표준 JSON 직렬화기를 사용하여 부호 없는 원시 유형(UInt, ULong, UByte 및 UShort)에 대해 사용할 수 있게 됨.

