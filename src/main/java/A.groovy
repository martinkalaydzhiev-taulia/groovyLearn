class A {
    static void main(String[] args) {
        def s1 = "aaaa"
        def s2 = "bbbb"
        (s1, s2) = [s2, s1]
        println("$s1 and $s2")

        def (a1, b2) = ["ppp", "ooo", "kkk"]
        assert a1 == "ppp"
        assert b2 == "ooo"
        println("$a1 $b2")

        def (a2, a3, a4) = ["kkk", "lll"]
        assert a4 == null

        def a5 = ["ddd"]
        a5 << "dd"
        assert a5 == ["ddd", "dd"]

        def c1 = new ComplexNumber(real:  1, imaginary:  5)
        def c2 = new ComplexNumber(real: 2, imaginary: 3)
        println(c1 + c2)
        println(c1 - c2)

        def company = new StringBuilder("Google")
        def price = 683.10
        /*def companyClosure = {i -> i.write(company)}
        def companyPrice = {i -> i.write("$price")}*/
        def stocks = ["Google": 683.10, "Apple": 620.3, "Amazon": 590.9]
        //stocks.each {key, value -> println("Today ${companyClosure} is at ${companyPrice}")}

        def companyClosure = { -> company }
        def priceClosure = { -> price }

        def quote = "Today ${companyClosure} stock closed at ${priceClosure}"

        stocks.each { key, value ->
            company = key
            price = value
            println quote
        }

        def q2 = "Today ${ -> company} stock closed at ${ -> price}"
        stocks.each {key, value ->
            company = key
            price = value
            println(q2)
        }

        def counries = ["Bulgaria" : "Sofia", "Germany" : "Berlin", "United States" : "Washington DC", "United Kingdom" : "London"]
        counries.each {country, capital ->
            def message = """
                <Country $country>
                <Capital $capital>
                ------------------
                """
            println(message)
        }

        for (str in "Mo".."Mq") {
            println(str)
        }

        def mes1 = "A1 Ring, Silverstone, Monaco, Nuerburgring"
        println(mes1)
        mes1 -= "Silverstone"
        mes1 += ", Spa"
        println(mes1)

        def pattern = ~"(G|g)roovy"
        def text = "AD ASDasD qweqeq 123213 5 Groov"
        def matcher = text =~ pattern
        println(matcher.size())


        def pattern2 = "M(ax|Ax|aX|AX)"
        def text2 = "MaaX MAXXX Max mAX MaX"
        def matcher2 = text2 =~ pattern2
        println("${matcher2.size()}")
        matcher2.each {m -> println(m)}

        println(text2 == ~pattern2)

        def text3 = "asd qwe asd qwe pp asd qwe"
        println(text3)
        text3 = (text3 =~ /asd/).replaceAll("123")
        println(text3)

        def list1 = [1, 2, 3, 4, 5, 6]
        def list2 = list1[1..4]
        println(list1)
        println(list2)
        list1[1] = 10
        println("$list1 $list2")

        list1.eachWithIndex {value, index ->
            list1[index] = value * 2
        }
        println(list1)
    }
}

class ComplexNumber {
    def real
    def imaginary


    def plus(other) {
        new ComplexNumber(real: real + other.real, imaginary: imaginary + other.imaginary)
    }

    def minus(other) {
        new ComplexNumber(real: real - other.real, imaginary: imaginary - other.imaginary)
    }

    String toString() {
        "$real ${imaginary > 0 ? '+' : ''} ${imaginary}i"
    }
}
