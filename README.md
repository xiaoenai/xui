# xui
Android常用UI、工具集合

# Usage

```aidl
implementation 'com.github.xiaoenai:xui:v0.0.1'
```

# 组件

- 可移动的view

```aidl
<com.xiaoenai.xui.MoveFrameLayout
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:background="#80000000">

    <TextView
        android:id="@+id/tv_html"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:paddingHorizontal="10dp"
        android:paddingTop="5dp"
        android:paddingBottom="5dp"
        tools:text="hello"
        tools:textColor="#000"
        tools:textSize="14sp" />
</com.xiaoenai.xui.MoveFrameLayout>
```