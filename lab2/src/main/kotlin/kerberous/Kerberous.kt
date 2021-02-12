package kerberous

fun runChain(c: Client, aServer: AServer, sServer: SServer, tgServer: TGServer) {
	
	val s1 = c.step1()
	println("C->AS: $s1")
	val s2 = aServer.step2(s1)
	println("AS->C: $s2")
	if (s2 == null) {
		println("Error on step 2")
		return
	}
	val s3 = c.step3(s2, sServer.id)
	println("C->TGS: $s3")
	if (s3 == null) {
		println("Error on step 3")
		return
	}
	val s4 = tgServer.step4(s3)
	println("TGS->C: $s4")
	if (s4 == null) {
		println("Error on step 4")
		return
	}
	val s5 = c.step5(s4)
	println("C->SS: $s5")
	val s6 = sServer.step6(s5)
	println("SS->C: $s6")
	val res = c.step6Check(s6)
	if (res){
		println("Final res: OK")
	}else{
		println("Final res: Fail")
	}
}