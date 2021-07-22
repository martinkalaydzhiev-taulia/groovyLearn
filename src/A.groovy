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
    }
}
