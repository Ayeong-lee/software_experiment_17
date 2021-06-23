// Generated code from Butter Knife. Do not modify!
package com.example.software_exp_17;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Calendar_ViewBinding implements Unbinder {
  private Calendar target;

  @UiThread
  public Calendar_ViewBinding(Calendar target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Calendar_ViewBinding(Calendar target, View source) {
    this.target = target;

    target.materialCalendarView = Utils.findRequiredViewAsType(source, R.id.calendarView, "field 'materialCalendarView'", MaterialCalendarView.class);
    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Calendar target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.materialCalendarView = null;
    target.textView = null;
  }
}
