/*
 * Copyright (c) 2011 Socialize Inc. 
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.socialize.ui.integrationtest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;
import com.socialize.ui.sample.SampleActivity;

/**
 * @author Jason Polites
 */
public abstract class SocializeUIRobotiumTest extends ActivityInstrumentationTestCase2<SampleActivity> {

	public static final int DEFAULT_TIMEOUT_SECONDS = 100;
	public static final String DEFAULT_ENTITY_URL = "http://socialize.integration.tests.com?somekey=somevalue&anotherkey=anothervalue";
	public static final String DEFAULT_GET_ENTITY = "http://entity1.com";
	public static final String SOCIALIZE_FACEBOOK_ID = "209798315709193";
		
	protected Solo robotium;
	protected InputMethodManager imm = null;


	public SocializeUIRobotiumTest() {
		super("com.socialize.ui.sample", SampleActivity.class);
	}

	public void setUp() throws Exception {
		imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		robotium = new Solo(getInstrumentation(), getActivity());
		robotium.waitForActivity("SampleActivity", 5000);
	}
	
	protected void startWithFacebook(boolean sso) {
		robotium.clearEditText(0);
		robotium.enterText(0, DEFAULT_GET_ENTITY);
		robotium.clearEditText(1);
		robotium.enterText(1, SOCIALIZE_FACEBOOK_ID);
		
		if(!sso) {
			robotium.clickOnButton(0);
		}
		
		robotium.clickOnButton(1);
		
		robotium.waitForActivity("CommentActivity", 5000);
		robotium.waitForView(ListView.class, 1, 5000);
		sleep(2000);
	}
	
	protected void startWithoutFacebook() {
		robotium.clearEditText(0);
		robotium.enterText(0, DEFAULT_GET_ENTITY);
		robotium.clearEditText(1);
		robotium.clickOnButton(0);
		robotium.clickOnButton(1);
		robotium.waitForActivity("CommentActivity", 5000);
		robotium.waitForView(ListView.class, 1, 5000);
		
		sleep(5000);
	}

	@Override
	protected void tearDown() throws Exception {
//		try {
//			robotium.finalize();
//		} 
//		catch (Throwable e) {
//			throw new Exception(e);
//		}
		
		ArrayList<Activity> allOpenedActivities = robotium.getAllOpenedActivities();
		
		for (Activity activity : allOpenedActivities) {
			activity.finish();
		}
		
		robotium.getActivityMonitor().getLastActivity().finish();
		getActivity().finish();
		super.tearDown();
	}


	protected final void sleep(int milliseconds) {
		synchronized (this) {
			try {
				wait(milliseconds);
			}
			catch (InterruptedException ignore) {}
		}
	}

	protected final void hideKeyboard() {
		// Hide keyboard for all
		ArrayList<EditText> currentEditTexts = robotium.getCurrentEditTexts();

		if(currentEditTexts != null) {
			for (EditText editText : currentEditTexts) {
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			}
		}
	}

}
