package com.sample_mvvm.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sample_mvvm.R;
import com.sample_mvvm.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Stack;


/**
 * Created by admin 22/11/2016
 */
public class FragmentHelper {

    public ArrayList<Stack<BaseFragment>> pageList;
    protected int pageIndex;
    protected int pageSize;
    protected int layoutId;
    protected int childPage = -1;
    protected FragmentManager fragmentManager;

    protected BaseFragment[] buildFragments;

    FragmentAction fragmentAction;

    public int getLayoutId() {
        return layoutId;
    }

    public Fragment[] getBuildFragments() {
        return buildFragments;
    }

    public FragmentHelper(FragmentAction fragmentAction) {
        this.fragmentAction = fragmentAction;
        this.layoutId = fragmentAction.getFrameLayoutId();
        this.fragmentManager = fragmentAction.setFragmentManager();
        this.buildFragments = fragmentAction.initFragment();
        initFragments(buildFragments);
    }

    private void initFragments(BaseFragment[] fragments) {
        this.pageList = new ArrayList<Stack<BaseFragment>>(fragments.length);
        pageSize = fragments.length;

        for (BaseFragment fragment : fragments) {
            Stack<BaseFragment> stack = new Stack<>();
            stack.push(fragment);
            this.pageList.add(stack);
        }

        BaseFragment fragment = pageList.get(pageIndex).peek();
        if (fragment.isAdded() || fragment.isDetached()) {
            this.showFragment(pageIndex);
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(layoutId, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    public BaseFragment peek() {
        return pageList.get(pageIndex).peek();
    }

    public void pushFragment(BaseFragment fragment) {
        BaseFragment showFragment = fragment;
        BaseFragment hideFragment = pageList.get(pageIndex).peek();
        pageList.get(pageIndex).push(showFragment);

        showFragment.isCurrentScreen = true;
        hideFragment.isCurrentScreen = true;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(layoutId, showFragment);
        transaction.hide(hideFragment);
        transaction.commitAllowingStateLoss();

    }

    public boolean popFragment() {
        return popFragment(1);
    }

    public void replaceFragment(MainActivity self, Fragment fragment) {
        FragmentManager fragmentManager = self.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.enter,
                        R.anim.exit,
                        0, 0)
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public boolean popFragmentToRoot() {
        int level = pageList.get(pageIndex).size() - 1;
        return popFragment(level);
    }

    public boolean popFragment(int level) {
        if (level <= 0) return false;
        if (pageList.get(pageIndex).size() <= level) return false;
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        while (level >= 1) {
            BaseFragment fragment = pageList.get(pageIndex).pop();
            fragment.isCurrentScreen = true;
            transaction.remove(fragment);
            level--;
        }
        BaseFragment showFragment = pageList.get(pageIndex).peek();
        showFragment.isCurrentScreen = true;
        transaction.show(showFragment);
        transaction.commitAllowingStateLoss();
        return true;
    }


    public <E extends BaseFragment> boolean popToFragment(Class<E> aClass) {
        int level = findIndexFragmentChild(aClass);
        return popFragment(level);
    }

    public <E extends BaseFragment> int findIndexFragmentChild(Class<E> aClass) {
        int size = getCurrentPageSize(), level = 0;
        for (int i = size - 1; i >= 0; i--) {
            BaseFragment baseFragment = pageList.get(pageIndex).get(i);
            if (aClass.getSimpleName().equals(baseFragment.getClass().getSimpleName())) {
                return level;
            } else {
                level++;
            }
        }
        return -1;
    }


    public void showFragment(int index) {
        if (index == pageIndex) return;
        BaseFragment showFragment = pageList.get(index).peek();
        BaseFragment hideFragment = pageList.get(pageIndex).peek();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (pageIndex > index) {
            showFragment.isInLeft = true;
            hideFragment.isOutLeft = false;
        } else {
            showFragment.isInLeft = false;
            hideFragment.isOutLeft = true;
        }
        showFragment.isCurrentScreen = false;
        hideFragment.isCurrentScreen = false;

        // Khong refresh lai UI, ko refresh lai data
        if (showFragment.isDetached() || showFragment.isAdded()) {
            transaction.show(showFragment);
        } else {
            transaction.add(layoutId, showFragment);
        }
        transaction.hide(hideFragment);

        //refresh lai UI, refresh lai data
//        if(showFragment.isDetached() || showFragment.isAdded()){
//            transaction.attach(showFragment);
//        }else{
//            transaction.add(layoutId, showFragment);
//        }
//        transaction.detach(hideFragment);


        transaction.commitAllowingStateLoss();

        pageIndex = index;
    }


    public int getCurrentPageSize() {
        return pageList.get(pageIndex).size();
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int index) {
        pageIndex = index;
    }

    public void setChildPage(int childPage) {
        this.childPage = childPage;
    }

    public void remove() {
        if (buildFragments != null && buildFragments.length > 0) {
            for (BaseFragment fragment : buildFragments) {
                fragment = null;
            }
        }
        buildFragments = null;
        fragmentAction = null;
        fragmentManager = null;
    }

    public interface FragmentAction {
        BaseFragment[] initFragment();

        int getFrameLayoutId();

        FragmentManager setFragmentManager();
    }
}
