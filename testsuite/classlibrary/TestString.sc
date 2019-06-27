TestString : UnitTest {

	// ------- path-like operations ---------------------------------------------

	test_withTrailingSlash_onEmptyString_addsSeparator {
		var expected = thisProcess.platform.pathSeparator.asString;
		this.assertEquals("".withTrailingSlash, expected);
	}

	test_withTrailingSlash_onPathSeparator_isNoop {
		var sep = thisProcess.platform.pathSeparator.asString;
		this.assertEquals(sep.withTrailingSlash, sep);
	}

	test_withoutTrailingSlash_onEmptyString_isNoop {
		this.assertEquals("".withoutTrailingSlash, "");
	}

	test_withoutTrailingSlash_onSomeString_isNoop {
		var str = "hello";
		this.assertEquals(str.withoutTrailingSlash, str);
	}

	test_withoutTrailingSlash_onSeparator_removesSep {
		var sep = thisProcess.platform.pathSeparator.asString;
		this.assertEquals(sep.withoutTrailingSlash, "");
	}

	// Windows should treat slash as a path sep
	test_withoutTrailingSlash_onSlash_removesSep {
		this.assertEquals("/".withoutTrailingSlash, "");
	}

	// operator +/+
	test_appendPathSep_emptyWithEmpty_producesSep {
		var sep = thisProcess.platform.pathSeparator.asString;
		this.assertEquals("" +/+ "", sep);
	}

	test_appendPathSep_nonSepWithNonSep_producesSep {
		var sep = thisProcess.platform.pathSeparator.asString;
		this.assertEquals("abc" +/+ "def", "abc" ++ sep ++ "def");
	}

	test_appendPathSep_sepWithNonSep_onlyOneSep {
		var sep = thisProcess.platform.pathSeparator.asString;
		var result = ("abc" ++ sep) +/+ ("def");
		var expected = "abc" ++ sep ++ "def";
		this.assertEquals(result, expected);
	}

	test_appendPathSep_nonSepWithSep_onlyOneSep {
		var sep = thisProcess.platform.pathSeparator.asString;
		var result = ("abc") +/+ (sep ++ "def");
		var expected = "abc" ++ sep ++ "def";
		this.assertEquals(result, expected);
	}

	test_appendPathSep_sepWithSep_onlyOneSep {
		var sep = thisProcess.platform.pathSeparator.asString;
		var result = ("abc" ++ sep) +/+ (sep ++ "def");
		var expected = "abc" ++ sep ++ "def";
		this.assertEquals(result, expected);
	}

	// Windows should accept / as a path separator in these cases, and prefer using the LHS slash
	test_appendPathSep_slashWithSlash_onlyOneSep {
		var result = "abc/" +/+ "/def";
		var expected = "abc/def";
		this.assertEquals(result, expected);
	}

	test_appendPathSep_slashWithBackslash_onlyOneSep {
		var result = "abc/" +/+ "\\def";
		var expected = thisProcess.platform.isPathSeparator($\\).if { "abc/def" } { "abc/\\def" };
		this.assertEquals(result, expected);
	}

	test_appendPathSep_backslashWithBackslash {
		var result = "abc\\" +/+ "\\def";
		var expected = thisProcess.platform.isPathSeparator($\\).if { "abc\\def" } { "abc\\/\\def" };
		this.assertEquals(result, expected);
	}

	test_appendPathSep_stringWithPathName_convertsToPathName {
		var result = "abc" +/+ PathName("def");
		var expected = PathName("abc" +/+ "def");
		this.assertEquals(result.fullPath, expected.fullPath);
	}

	// should work with symbols too for backward compatibility
	test_appendPathSep_stringWithSymbol_producesString {
		var sep = thisProcess.platform.pathSeparator.asString;
		this.assertEquals("dir" +/+ 'file', "dir%file".format(sep));
	}

	// regression tests for #4252
	test_standardizePath_withTrailingSlash_shouldNotRemove {
		var result = "~/".standardizePath;
		var expected = "~".standardizePath ++ "/";
		this.assertEquals(result, expected);
	}

	test_standardizePath_withTwoTrailingSlashes_shouldNotRemove {
		var result = "~//".standardizePath;
		var expected = "~".standardizePath ++ "//";
		this.assertEquals(result, expected);
	}

	test_standardizePath_tilde_expandsToHome {
		var result = "~".standardizePath;
		var expected = Platform.userHomeDir;
		this.assertEquals(result, expected);
	}

	// ------- time-related operations -----------------------------------------------

	test_asSecs_stringDddHhMmSsSss_convertsToSeconds {
		var result = "001:01:01:01.001".asSecs;
		var expected = 90061.001;
		this.assertEquals(result, expected);
	}

	test_asSecs_stringSsSss_convertsToSeconds {
		var result = "01.001".asSecs;
		var expected = 1.001;
		this.assertEquals(result, expected);
	}

	test_asSecs_stringMmSs_convertsToSeconds {
		var result = "01:01".asSecs;
		var expected = 61.0;
		this.assertEquals(result, expected);
	}

	test_asSecs_stringSs_convertsToSeconds {
		var result = "01".asSecs;
		var expected = 1.0;
		this.assertEquals(result, expected);
	}

	test_findRegexp_nonEmptyResult {
		var result = "two words".findRegexp("[a-zA-Z]+");
		this.assertEquals(
			result,
			[[0, "two"], [4, "words"]],
			"`\"two words\".findRegexp(\"[a-zA-Z]+\")` should return a nested array of indices and matches"
		)
	}

	test_findRegexp_emptyResult {
		var result = "the quick brown fox".findRegexp("moo");
		this.assertEquals(result, Array.new, "Non-matching findRegexp should return empty array");
	}

	// ----- fuzzy string comparisons --------------------------------------------

	test_editDistance_emptyThis_isSizeThat {
		var result = "".editDistance("hello");
		var expected = 5;

		this.assertEquals(result, expected);
	}

	test_editDistance_emptyThat_isSizeThis {
		var result = "hello".editDistance("");
		var expected = 5;

		this.assertEquals(result, expected);
	}

	test_editDistance_emptyThis_emptyThat_isZero {
		var result = "".editDistance("");
		var expected = 0;

		this.assertEquals(result, expected);
	}

	test_editDistance_countsSubstitution {
		var result = "hello".editDistance("hallo");
		var expected = 1;

		this.assertEquals(result, expected);
	}

	test_editDistance_countsAddition {
		var result = "hello".editDistance("helloo");
		var expected = 1;

		this.assertEquals(result, expected);
	}

	test_editDistance_countsRemoval {
		var result = "hello".editDistance("hell");
		var expected = 1;

		this.assertEquals(result, expected);
	}

	test_editDistance_countsChanges {
		var result = "hello".editDistance("string");
		var expected = 6; // h:s, e:t, l:r, l:i, o:n, addition:g

		this.assertEquals(result, expected);
	}

	test_similarity_emptyThis_emptyThat_isOne {
		var result = "".similarity("");
		var expected = 1; // they're the same, even though they're empty

		this.assertEquals(result, expected);
	}

	test_similarity_sameWord_isOne {
		var result = "hello".similarity("hello");
		var expected = 1;

		this.assertEquals(result, expected);
	}

	test_similarity_differentWord_isZero {
		var result = "hello".similarity("asdf");
		var expected = 0;

		this.assertEquals(result, expected);
	}

	test_isSimilar_emptyThis_emptyThat_isTrue {
		var result = "".isSimilar("");
		var expected = true;

		this.assertEquals(result, expected);
	}

	test_isSimilar_sameWord_isTrue {
		var result = "hello".isSimilar("hello");
		var expected = true;

		this.assertEquals(result, expected);
	}

	test_isSimilar_differentWord_isFalse {
		var result = "hello".isSimilar("asdf");
		var expected = false;

		this.assertEquals(result, expected);
	}

	test_isSimilar_similarWordWithinDelta_isTrue {
		var result = "word".isSimilar("wodr"); // 50% change
		var expected = true;

		this.assertEquals(result, expected);
	}

	test_isSimilar_similarWordOutsideDelta_isFalse {
		var result = "word".isSimilar("wraps"); // >50% change
		var expected = false;

		this.assertEquals(result, expected);
	}

}
