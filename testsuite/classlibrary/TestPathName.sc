TestPathName : UnitTest {
	test_absNixPathOnNix_isAbsolute {
		this.skipOnPredicate( { thisProcess.platform.name == \windows },
			{
				var result = PathName("/").isAbsolutePath;
				var expected = true;
				this.assertEquals(result, expected, "");
			},
			thisMethod
		);
	}

	test_absNixPathOnWin_isAbsolute {
		this.skipOnPredicate( { thisProcess.platform.name != \windows },
			{
				var result = PathName("C:/").isAbsolutePath;
				var expected = true;
				this.assertEquals(result, expected, "");
			},
			thisMethod
		);
	}

	test_absWinPathOnWin_isAbsolute {
		this.skipOnPredicate( { thisProcess.platform.name != \windows },
			{
				var result = PathName("C:\\").isAbsolutePath;
				var expected = true;
				this.assertEquals(result, expected, "");
			},
			thisMethod
		);
	}

	test_absWinMachinePathOnWin_isAbsolute {
		this.skipOnPredicate( { thisProcess.platform.name != \windows},
			{
				var result = PathName("\\\\machinename\\").isAbsolutePath;
				var expected = true;
				this.assertEquals(result, expected, "");
			},
			thisMethod
		);
	}
}