package com.mobileapps.moviefinder;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainUITest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.registerPage), withText("Create New Account"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Sam Baker"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("baker@email.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.passwordConfirm),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.passwordConfirm), withText("123456"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText5.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.passwordConfirm), withText("123456"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("123456"));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.passwordConfirm), withText("123456"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText7.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.registerBtn), withText("Create"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                8),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.account), withContentDescription("Account Info"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.userEmail), withText("baker@email.com"),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        textView.check(matches(withText("baker@email.com")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.userName), withText("Sam Baker"),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        textView2.check(matches(withText("Sam Baker")));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.updateAccount), withText("Update Account Info"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                6),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.updatedEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("new@email.com"), closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.updateEmailBtn), withText("Update Email"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                6),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.updatedName),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                10),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("New Name"), closeSoftKeyboard());

        pressBack();

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.updateNameBtn), withText("Update Name"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                11),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.account), withContentDescription("Account Info"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.userEmail), withText("new@email.com"),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        textView3.check(matches(withText("new@email.com")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.userName), withText("New Name"),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        textView4.check(matches(withText("New Name")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.userName), withText("New Name"),
                        withParent(withParent(withId(R.id.fragment_container))),
                        isDisplayed()));
        textView5.check(matches(withText("New Name")));

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.about), withContentDescription("About/Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                1),
                        isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                2),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.title), withText("Watch Later"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction overflowMenuButton2 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                2),
                        isDisplayed()));
        overflowMenuButton2.perform(click());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.title), withText("Previously Watched"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction overflowMenuButton3 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                2),
                        isDisplayed()));
        overflowMenuButton3.perform(click());

        ViewInteraction materialTextView3 = onView(
                allOf(withId(R.id.title), withText("Home/Sign Out"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView3.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.findMoviePage), withText("Find Movie(s)"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.button), withText("Generate Movies"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.expandButton), withText("Show More Info"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card),
                                        0),
                                2),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.addWatchLaterBtn), withText("Watch Later List"),
                        childAtPosition(
                                allOf(withId(R.id.addListLayout),
                                        childAtPosition(
                                                withId(R.id.extraInfo),
                                                3)),
                                2),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.addPrevWatchedBtn), withText("Previously Watched List"),
                        childAtPosition(
                                allOf(withId(R.id.addListLayout),
                                        childAtPosition(
                                                withId(R.id.extraInfo),
                                                3)),
                                1),
                        isDisplayed()));
        materialButton10.perform(click());

        pressBack();

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.expandButton), withText("Show More Info"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card),
                                        0),
                                2),
                        isDisplayed()));
        materialButton11.perform(click());

        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.addPrevWatchedBtn), withText("Previously Watched List"),
                        childAtPosition(
                                allOf(withId(R.id.addListLayout),
                                        childAtPosition(
                                                withId(R.id.extraInfo),
                                                3)),
                                1),
                        isDisplayed()));
        materialButton12.perform(click());

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.addWatchLaterBtn), withText("Watch Later List"),
                        childAtPosition(
                                allOf(withId(R.id.addListLayout),
                                        childAtPosition(
                                                withId(R.id.extraInfo),
                                                3)),
                                2),
                        isDisplayed()));
        materialButton13.perform(click());

        ViewInteraction overflowMenuButton4 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                2),
                        isDisplayed()));
        overflowMenuButton4.perform(click());

        ViewInteraction materialTextView4 = onView(
                allOf(withId(R.id.title), withText("Watch Later"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView4.perform(click());

        ViewInteraction linearLayoutCompat = onView(
                allOf(withParent(allOf(withId(R.id.postersRecyclerView),
                        withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        linearLayoutCompat.check(matches(isDisplayed()));

        ViewInteraction linearLayoutCompat2 = onView(
                allOf(withParent(allOf(withId(R.id.postersRecyclerView),
                        withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        linearLayoutCompat2.check(matches(isDisplayed()));

        ViewInteraction overflowMenuButton5 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                2),
                        isDisplayed()));
        overflowMenuButton5.perform(click());

        ViewInteraction materialTextView5 = onView(
                allOf(withId(R.id.title), withText("Previously Watched"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView5.perform(click());

        ViewInteraction linearLayoutCompat3 = onView(
                allOf(withParent(allOf(withId(R.id.postersRecyclerView),
                        withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        linearLayoutCompat3.check(matches(isDisplayed()));

        ViewInteraction linearLayoutCompat4 = onView(
                allOf(withParent(allOf(withId(R.id.postersRecyclerView),
                        withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        linearLayoutCompat4.check(matches(isDisplayed()));

        pressBack();

        ViewInteraction overflowMenuButton6 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                2),
                        isDisplayed()));
        overflowMenuButton6.perform(click());

        ViewInteraction materialTextView6 = onView(
                allOf(withId(R.id.title), withText("Home/Sign Out"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView6.perform(click());

        ViewInteraction materialButton14 = onView(
                allOf(withId(R.id.logoutBtn), withText("Sign Out"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton14.perform(click());

        ViewInteraction materialButton15 = onView(
                allOf(withId(R.id.loginPage), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton15.perform(click());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                allOf(withId(R.id.textView),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("new@email.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                allOf(withId(R.id.textView),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction materialButton16 = onView(
                allOf(withId(R.id.loginBtn), withText("Login"),
                        childAtPosition(
                                allOf(withId(R.id.textView),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                6),
                        isDisplayed()));
        materialButton16.perform(click());

        ViewInteraction materialButton17 = onView(
                allOf(withId(R.id.logoutBtn), withText("Sign Out"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton17.perform(click());

        ViewInteraction materialButton18 = onView(
                allOf(withId(R.id.registerPage), withText("Create New Account"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton18.perform(click());

        ViewInteraction materialTextView7 = onView(
                allOf(withId(R.id.alreadyRegistered), withText("Already have an account? Login here!"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                6),
                        isDisplayed()));
        materialTextView7.perform(click());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                allOf(withId(R.id.textView),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("new@email.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                allOf(withId(R.id.textView),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText13.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction materialButton19 = onView(
                allOf(withId(R.id.loginBtn), withText("Login"),
                        childAtPosition(
                                allOf(withId(R.id.textView),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                6),
                        isDisplayed()));
        materialButton19.perform(click());

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.account), withContentDescription("Account Info"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView4.perform(click());

        ViewInteraction materialButton20 = onView(
                allOf(withId(R.id.deleteAccount), withText("Delete Account"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container),
                                        0),
                                8),
                        isDisplayed()));
        materialButton20.perform(click());

        ViewInteraction materialButton21 = onView(
                allOf(withId(android.R.id.button1), withText("YES"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton21.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
