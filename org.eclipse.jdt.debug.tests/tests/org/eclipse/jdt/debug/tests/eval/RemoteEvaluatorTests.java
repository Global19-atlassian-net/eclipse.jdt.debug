/*******************************************************************************
 * Copyright (c) 2020 Gayan Perera and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Gayan Perera - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.debug.tests.eval;

import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.tests.AbstractDebugTest;

public class RemoteEvaluatorTests extends AbstractDebugTest {
	private IJavaThread javaThread;

	public RemoteEvaluatorTests(String name) {
		super(name);
	}

	public void testEvaluate_InInnerScope_FieldInOuterScope() throws Exception {
		debugWithBreakpoint("RemoteEvaluator", 20);

		String code = "java.util.Arrays.asList(\"a\", \"b\", \"ac\").stream().filter(v -> RemoteEvaluator.P_EMPTY.test(v)).count()";
		IValue value = evaluate(code);

		assertNotNull("result is null", value);
		assertEquals("count is not 0", "0", value.getValueString());
	}


	public void testEvaluate_InOuterScope_FieldInSameScope() throws Exception {
		debugWithBreakpoint("RemoteEvaluator", 12);
		IValue value = evaluate("java.util.Arrays.asList(\"a\", \"b\", \"ac\").stream().filter(v -> RemoteEvaluator.P_EMPTY.test(v)).count()");

		assertNotNull("result is null", value);
		assertEquals("count is not 0", "0", value.getValueString());
	}

	public void testEvaluate_InInnerScope_PrivateFieldInSameScope() throws Exception {
		debugWithBreakpoint("RemoteEvaluator", 20);
		IValue value = evaluate("java.util.Arrays.asList(\"a\", \"b\", \"ac\").stream().filter(v -> this.Q_EMPTY.test(v)).count()");

		assertNotNull("result is null", value);
		assertEquals("count is not 0", "0", value.getValueString());
	}

	public void testEvaluate_InInnerScope_PrivateFieldInSameScope_WithoutThis() throws Exception {
		debugWithBreakpoint("RemoteEvaluator", 20);
		IValue value = evaluate("java.util.Arrays.asList(\"a\", \"b\", \"ac\").stream().filter(v -> Q_EMPTY.test(v)).count()");

		assertNotNull("result is null", value);
		assertEquals("count is not 0", "0", value.getValueString());
	}

	public void testEvaluate_AnonymousClassInstance_WithSemicolon() throws Exception {
		debugWithBreakpoint("AnonymousEvaluator", 6);
		IValue value = evaluate("calculate(new IntSupplier() {\n" + 
				"\n" + 
				"			@Override\n" + 
				"			public int getAsInt() {\n" + 
				"				return 100 * 20;\n" + 
				"			}\n" + 
				"		});");

		assertNotNull("result is null", value);
		assertEquals("count is not 4000", "4000", value.getValueString());
	}

	public void testEvaluate_AnonymousClassInstance_WithoutSemicolon() throws Exception {
		debugWithBreakpoint("AnonymousEvaluator", 6);
		IValue value = evaluate("calculate(new IntSupplier() {\n" + 
				"\n" + 
				"			@Override\n" + 
				"			public int getAsInt() {\n" + 
				"				return 100 * 20;\n" + 
				"			}\n" + 
				"		})");

		assertNotNull("result is null", value);
		assertEquals("count is not 4000", "4000", value.getValueString());
	}

	public void testEvaluate_PrimitiveCastInLamndaExpr_ShouldEvaluatedWithCastType() throws Exception {
		debugWithBreakpoint("RemoteEvaluator", 20);
		IValue value = evaluate("java.util.stream.IntStream.of(1,2,3).anyMatch(i -> ((short) i) > (short)2)");

		assertNotNull("result is null", value);
		assertEquals("value is not true", "true", value.getValueString());
	}

	public void testEvaluate_TypeCastInLamndaExpr_ShouldEvaluatedWithCastType() throws Exception {
		debugWithBreakpoint("RemoteEvaluator", 20);
		IValue value = evaluate("java.util.stream.Stream.<java.util.List<Object>>of(new java.util.ArrayList<>()).anyMatch(l -> ((java.util.ArrayList<Object>)l).size() > 1)");

		assertNotNull("result is null", value);
		assertEquals("value is not false", "false", value.getValueString());
	}

	@Override
	protected IJavaProject getProjectContext() {
		return get18Project();
	}

	private void debugWithBreakpoint(String testClass, int lineNumber) throws Exception {
		createLineBreakpoint(lineNumber, testClass);
		javaThread = launchToBreakpoint(testClass);
		assertNotNull("The program did not suspend", javaThread);
	}

	private IValue evaluate(String snippet) throws Exception {
		return doEval(javaThread, snippet);
	}

	@Override
	protected void tearDown() throws Exception {
		try {
			terminateAndRemove(javaThread);
		} finally {
			super.tearDown();
			removeAllBreakpoints();
		}
	}
}
