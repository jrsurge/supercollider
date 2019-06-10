TestPathName : UnitTest {
	test_absNixPathOnNix_isAbsolute {
		var result = PathName("/").isAbsolutePath;
		var expected = true;

		this.skipOnPredicate( { thisProcess.platform.name == \windows }, thisMethod );

		this.assertEquals(result, expected, "")
	}

	test_absNixPathOnWin_isAbsolute {
		var result = PathName("C:/").isAbsolutePath;
		var expected = true;

		this.skipOnPredicate( { thisProcess.platform.name != \windows }, thisMethod );

		this.assertEquals(result, expected, "");
	}

	test_absWinPathOnWin_isAbsolute {
		var result = PathName("C:\\").isAbsolutePath;
		var expected = true;
		this.skipOnPredicate( { thisProcess.platform.name != \windows }, thisMethod );

		this.assertEquals(result, expected, "");
	}

	test_absWinMachinePathOnWin_isAbsolute {
		var result = PathName("\\\\machinename\\").isAbsolutePath;
		var expected = true;

		this.skipOnPredicate( { thisProcess.platform.name != \windows}, thisMethod );

		this.assertEquals(result, expected, "");
	}
}