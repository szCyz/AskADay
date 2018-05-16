package com.cyz.baseproject.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.Html
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import butterknife.ButterKnife
import com.cyz.askaday.MainActivity
import com.cyz.askaday.R
import com.cyz.baseproject.model.MaterialSimpleListItem
import com.cyz.baseproject.adapter.ButtonItemAdapter
import com.cyz.baseproject.adapter.MaterialSimpleListAdapter
import com.cyz.baseproject.dialog.*
import com.cyz.baseproject.internal.MDButton
import com.cyz.baseproject.internal.MDTintHelper
import com.cyz.baseproject.internal.ThemeSingleton
import com.cyz.baseproject.utils.DialogUtils
import com.cyz.baseproject.utils.GravityEnum
import com.cyz.baseproject.utils.StackingBehavior
import com.cyz.baseproject.utils.Theme
import com.cyz.baseproject.view.CircleView
import kotlinx.android.synthetic.main.activity_md_dialog_sample.*
import java.io.File

class MdDialogSampleActivity : AppCompatActivity() , FolderChooserDialog.FolderCallback,
        FileChooserDialog.FileCallback , ColorChooserDialog.ColorCallback{



    private val STORAGE_PERMISSION_RC = 69
    internal var index = 0
    // Custom View Dialog
    private var passwordInput: EditText? = null
    private var positiveAction: View? = null
    // color chooser dialog
    private var primaryPreselect: Int = 0
    // UTILITY METHODS
    private var accentPreselect: Int = 0
    private var toast: Toast? = null
    private var thread: Thread? = null
    private var handler: Handler? = null
    private var chooserDialog: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_md_dialog_sample)

        ButterKnife.bind(this)

        handler = Handler()
        primaryPreselect = DialogUtils.resolveColor(this, R.attr.colorPrimary)
        accentPreselect = DialogUtils.resolveColor(this, R.attr.colorAccent)

        basicNoTitle.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .content(R.string.shareLocationPrompt)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .show()
        })
        basic.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.useGoogleLocationServices)
                    .content(R.string.useGoogleLocationServicesPrompt, true)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .show()
        })
        basicLongContent.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.useGoogleLocationServices)
                    .content(R.string.loremIpsum)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .checkBoxPrompt("Hello, world!", true, null)
                    .show()
        })
        basicIcon.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .iconRes(R.mipmap.ic_launcher)
                    .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                    .title(R.string.useGoogleLocationServices)
                    .content(R.string.useGoogleLocationServicesPrompt, true)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .show()
        })
        basicCheckPrompt.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .iconRes(R.mipmap.ic_launcher)
                    .limitIconToDefaultSize()
                    .title(Html.fromHtml(getString(R.string.permissionSample, getString(R.string.app_name))))
                    .positiveText(R.string.allow)
                    .negativeText(R.string.deny)
                    .onAny { dialog, which -> showToast("Prompt checked? " + dialog.isPromptCheckBoxChecked) }
                    .checkBoxPromptRes(R.string.dont_ask_again, false, null)
                    .show()
        })
        stacked.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.useGoogleLocationServices)
                    .content(R.string.useGoogleLocationServicesPrompt, true)
                    .positiveText(R.string.speedBoost)
                    .negativeText(R.string.noThanks)
                    .btnStackedGravity(GravityEnum.END)
                    .stackingBehavior(
                            StackingBehavior
                                    .ALWAYS) // this generally should not be forced, but is used for demo purposes
                    .show()
        })
        neutral.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.useGoogleLocationServices)
                    .content(R.string.useGoogleLocationServicesPrompt, true)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .neutralText(R.string.more_info)
                    .show()
        })
        callbacks.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.useGoogleLocationServices)
                    .content(R.string.useGoogleLocationServicesPrompt, true)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .neutralText(R.string.more_info)
                    .onAny { dialog, which -> showToast(which.name + "!") }
                    .show()
        })
        list.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.socialNetworks)
                    .itemsCallback { dialog, view, which, text -> showToast(which.toString() + ": " + text) }
                    .show()
        })
        listNoTitle.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .items(R.array.socialNetworks)
                    .itemsCallback { dialog, view, which, text -> showToast(which.toString() + ": " + text) }
                    .show()
        })
        longList.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.states)
                    .items(R.array.states)
                    .itemsCallback { dialog, view, which, text -> showToast(which.toString() + ": " + text) }
                    .positiveText(android.R.string.cancel)
                    .show()
        })
        list_longItems.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.socialNetworks_longItems)
                    .itemsCallback { dialog, view, which, text -> showToast(which.toString() + ": " + text) }
                    .show()
        })
        list_checkPrompt.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.states)
                    .itemsCallback { dialog, view, which, text -> showToast(which.toString() + ": " + text + ", prompt: " + dialog.isPromptCheckBoxChecked) }
                    .checkBoxPromptRes(R.string.example_prompt, true, null)
                    .negativeText(android.R.string.cancel)
                    .show()
        })

        list_longPress.setOnClickListener(View.OnClickListener {
            index = 0
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.socialNetworks)
                    .itemsCallback { dialog, view, which, text -> showToast(which.toString() + ": " + text) }
                    .autoDismiss(false)
                    .itemsLongCallback { dialog, itemView, position, text ->
                        dialog.items!!.removeAt(position)
                        dialog.notifyItemsChanged()
                        false
                    }
                    .onNeutral { dialog, which ->
                        index++
                        dialog.items!!.add("Item $index")
                        dialog.notifyItemInserted(dialog.items!!.size - 1)
                    }
                    .neutralText(R.string.add_item)
                    .show()
        })
        singleChoice.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.socialNetworks)
                    .itemsDisabledIndices(1, 3)
                    .itemsCallbackSingleChoice(
                            2
                    ) { dialog, view, which, text ->
                        showToast(which.toString() + ": " + text)
                        true // allow selection
                    }
                    .positiveText(R.string.md_choose_label)
                    .show()
        })
        singleChoice_longItems.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.socialNetworks_longItems)
                    .itemsCallbackSingleChoice(
                            2
                    ) { dialog, view, which, text ->
                        showToast(which.toString() + ": " + text)
                        true // allow selection
                    }
                    .positiveText(R.string.md_choose_label)
                    .show()
        })
        multiChoice.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.socialNetworks)
                    .itemsCallbackMultiChoice(
                            arrayOf(1, 3)
                    ) { dialog, which, text ->
                        val str = StringBuilder()
                        for (i in 0 until which.size) {
                            if (i > 0) {
                                str.append('\n')
                            }
                            str.append(which[i])
                            str.append(": ")
                            str.append(text[i])
                        }
                        showToast(str.toString())
                        true // allow selection
                    }
                    .onNeutral { dialog, which -> dialog.clearSelectedIndices() }
                    .onPositive { dialog, which -> dialog.dismiss() }
                    .alwaysCallMultiChoiceCallback()
                    .positiveText(R.string.md_choose_label)
                    .autoDismiss(false)
                    .neutralText(R.string.clear_selection)
                    .show()
        })
        multiChoiceLimited.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.socialNetworks)
                    .itemsCallbackMultiChoice(
                            arrayOf(1)
                    ) { dialog, which, text ->
                        val allowSelectionChange = which.size <= 2 // limit selection to 2, the new (un)selection is included in the which
                        // array
                        if (!allowSelectionChange) {
                            showToast(this.resources.getString(R.string.selection_limit_reached))
                        }
                        allowSelectionChange
                    }
                    .positiveText(R.string.dismiss)
                    .alwaysCallMultiChoiceCallback() // the callback will always be called, to check if
                    // (un)selection is still allowed
                    .show()
        })
        multiChoiceLimitedMin.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.socialNetworks)
                    .itemsCallbackMultiChoice(
                            arrayOf(1)
                    ) { dialog, which, text ->
                        val allowSelectionChange = which.size >= 1 // selection count must stay above 1, the new (un)selection is included
                        // in the which array
                        if (!allowSelectionChange) {
                            showToast(this.resources.getString(R.string.selection_min_limit_reached))
                        }
                        allowSelectionChange
                    }
                    .positiveText(R.string.dismiss)
                    .alwaysCallMultiChoiceCallback() // the callback will always be called, to check if
                    // (un)selection is still allowed
                    .show()
        })
        multiChoice_longItems.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.socialNetworks_longItems)
                    .itemsCallbackMultiChoice(
                            arrayOf(1, 3)
                    ) { dialog, which, text ->
                        val str = StringBuilder()
                        for (i in 0 until which.size) {
                            if (i > 0) {
                                str.append('\n')
                            }
                            str.append(which[i])
                            str.append(": ")
                            str.append(text[i])
                        }
                        showToast(str.toString())
                        true // allow selection
                    }
                    .positiveText(R.string.md_choose_label)
                    .show()
        })
        multiChoice_disabledItems.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.socialNetworks)
                    .items(R.array.socialNetworks)
                    .itemsCallbackMultiChoice(
                            arrayOf(0, 1, 2)
                    ) { dialog, which, text ->
                        val str = StringBuilder()
                        for (i in 0 until which.size) {
                            if (i > 0) {
                                str.append('\n')
                            }
                            str.append(which[i])
                            str.append(": ")
                            str.append(text[i])
                        }
                        showToast(str.toString())
                        true // allow selection
                    }
                    .onNeutral { dialog, which -> dialog.clearSelectedIndices() }
                    .alwaysCallMultiChoiceCallback()
                    .positiveText(R.string.md_choose_label)
                    .autoDismiss(false)
                    .neutralText(R.string.clear_selection)
                    .itemsDisabledIndices(0, 1)
                    .show()
        })
        simpleList.setOnClickListener(View.OnClickListener {
            val adapter = MaterialSimpleListAdapter { dialog, index1, item -> showToast(item.getContent().toString()) }
            adapter.add(
                    MaterialSimpleListItem.Builder(this)
                            .content("username@gmail.com")
                            .icon(R.mipmap.ic_account_circle)
                            .backgroundColor(Color.WHITE)
                            .build())
            adapter.add(
                    MaterialSimpleListItem.Builder(this)
                            .content("user02@gmail.com")
                            .icon(R.mipmap.ic_account_circle)
                            .backgroundColor(Color.WHITE)
                            .build())
            adapter.add(
                    MaterialSimpleListItem.Builder(this)
                            .content(R.string.add_account)
                            .icon(R.mipmap.ic_content_add)
                            .iconPaddingDp(8)
                            .build())

            MaterialDialog.Builder(this).title(R.string.set_backup).adapter(adapter, null).show()
        })
        customListItems.setOnClickListener(View.OnClickListener {
            val adapter = ButtonItemAdapter(this, R.array.socialNetworks)
            adapter.setCallbacks(
                    { itemIndex -> showToast("Item clicked: $itemIndex") },
                    { buttonIndex -> showToast("Button clicked: $buttonIndex") })
            MaterialDialog.Builder(this).title(R.string.socialNetworks).adapter(adapter, null).show()

        })
        customView.setOnClickListener(View.OnClickListener {
            val dialog = MaterialDialog.Builder(this)
                    .title(R.string.googleWifi)
                    .customView(R.layout.dialog_customview, true)
                    .positiveText(R.string.connect)
                    .negativeText(android.R.string.cancel)
                    .onPositive { dialog1, which -> showToast("Password: " + passwordInput!!.getText().toString()) }
                    .build()

            positiveAction = dialog.getActionButton(DialogAction.POSITIVE)
            //noinspection ConstantConditions
            passwordInput = dialog.customView!!.findViewById(R.id.password)
            passwordInput!!.addTextChangedListener(
                    object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                            positiveAction!!.setEnabled(s.toString().trim { it <= ' ' }.length > 0)
                        }

                        override fun afterTextChanged(s: Editable) {}
                    })

            // Toggling the show password CheckBox will mask or unmask the password input EditText
            val checkbox = dialog.customView!!.findViewById<CheckBox>(R.id.showPassword)
            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                passwordInput!!.setInputType(
                        if (!isChecked) InputType.TYPE_TEXT_VARIATION_PASSWORD else InputType.TYPE_CLASS_TEXT)
                passwordInput!!.setTransformationMethod(
                        if (!isChecked) PasswordTransformationMethod.getInstance() else null)
            }

            val widgetColor = ThemeSingleton.get().widgetColor
            MDTintHelper.setTint(
                    checkbox, if (widgetColor == 0) ContextCompat.getColor(this, R.color.accent) else widgetColor)

            MDTintHelper.setTint(
                    passwordInput,
                    if (widgetColor == 0) ContextCompat.getColor(this, R.color.accent) else widgetColor)

            dialog.show()
            (positiveAction as MDButton?)!!.setEnabled(false) // disabled by default
        })

        customView_webView.setOnClickListener(View.OnClickListener {
            var accentColor = ThemeSingleton.get().widgetColor
            if (accentColor == 0) {
                accentColor = ContextCompat.getColor(this, R.color.accent)
            }
            ChangelogDialog.create(false, accentColor).show(supportFragmentManager, "changelog")
        })

        customView_datePicker.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.date_picker)
                    .customView(R.layout.dialog_datepicker, false)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .show()
        })

        colorChooser_primary.setOnClickListener(View.OnClickListener {
            ColorChooserDialog.Builder(this, R.string.color_palette)
                    .titleSub(R.string.colors)
                    .preselect(primaryPreselect)
                    .show()
        })

        colorChooser_accent.setOnClickListener(View.OnClickListener {
            ColorChooserDialog.Builder(this, R.string.color_palette)
                    .titleSub(R.string.colors)
                    .accentMode(true)
                    .preselect(accentPreselect)
                    .show()
        })

        colorChooser_customColors.setOnClickListener(View.OnClickListener {
            val subColors = arrayOf(intArrayOf(Color.parseColor("#EF5350"), Color.parseColor("#F44336"), Color.parseColor("#E53935")), intArrayOf(Color.parseColor("#EC407A"), Color.parseColor("#E91E63"), Color.parseColor("#D81B60")), intArrayOf(Color.parseColor("#AB47BC"), Color.parseColor("#9C27B0"), Color.parseColor("#8E24AA")), intArrayOf(Color.parseColor("#7E57C2"), Color.parseColor("#673AB7"), Color.parseColor("#5E35B1")), intArrayOf(Color.parseColor("#5C6BC0"), Color.parseColor("#3F51B5"), Color.parseColor("#3949AB")), intArrayOf(Color.parseColor("#42A5F5"), Color.parseColor("#2196F3"), Color.parseColor("#1E88E5")))

            ColorChooserDialog.Builder(this, R.string.color_palette)
                    .titleSub(R.string.colors)
                    .preselect(primaryPreselect)
                    .customColors(R.array.custom_colors, subColors)
                    .show()
        })

        colorChooser_customColorsNoSub.setOnClickListener(View.OnClickListener {
            ColorChooserDialog.Builder(this, R.string.color_palette)
                    .titleSub(R.string.colors)
                    .preselect(primaryPreselect)
                    .customColors(R.array.custom_colors, null)
                    .show()
        })

        themed.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.useGoogleLocationServices)
                    .content(R.string.useGoogleLocationServicesPrompt, true)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .positiveColorRes(R.color.material_red_400)
                    .negativeColorRes(R.color.material_red_400)
                    .titleGravity(GravityEnum.CENTER)
                    .titleColorRes(R.color.material_red_400)
                    .contentColorRes(android.R.color.white)
                    .backgroundColorRes(R.color.material_blue_grey_800)
                    .dividerColorRes(R.color.accent)
                    .btnSelector(R.drawable.md_btn_selector_custom, DialogAction.POSITIVE)
                    .positiveColor(Color.WHITE)
                    .negativeColorAttr(android.R.attr.textColorSecondaryInverse)
                    .theme(Theme.DARK)
                    .show()
        })

        showCancelDismiss.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.useGoogleLocationServices)
                    .content(R.string.useGoogleLocationServicesPrompt, true)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .neutralText(R.string.more_info)
                    .showListener { dialog -> showToast("onShow") }
                    .cancelListener { dialog -> showToast("onCancel") }
                    .dismissListener { dialog -> showToast("onDismiss") }
                    .show()
        })

        file_chooser.setOnClickListener(View.OnClickListener {
            chooserDialog = R.id.file_chooser
            if (ActivityCompat.checkSelfPermission(
                            this@MdDialogSampleActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this@MdDialogSampleActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_RC)
            }
            FileChooserDialog.Builder(this).show()
        })

        folder_chooser.setOnClickListener(View.OnClickListener {
            chooserDialog = R.id.folder_chooser
            if (ActivityCompat.checkSelfPermission(
                            this@MdDialogSampleActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this@MdDialogSampleActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_RC)
            }
            FolderChooserDialog.Builder(this@MdDialogSampleActivity)
                    .chooseButton(R.string.md_choose_label)
                    .allowNewFolder(true, 0)
                    .show()
        })

        input.setOnClickListener(View.OnClickListener {
             MaterialDialog.Builder(this)
                .title(R.string.input)
                .content(R.string.input_content)
                .inputType(
                    InputType.TYPE_CLASS_TEXT
                        or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                        or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .inputRange(2, 16)
                        .positiveText(R.string.submit)
                        .input(
                            R.string.input_hint,
                            R.string.input_hint,
                            false
                        ) { dialog, input-> showToast("Hello, " + input.toString() + "!") }
                        .show()
                        })

        input_custominvalidation.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.input)
                    .content(R.string.input_content_custominvalidation)
                    .inputType(
                            InputType.TYPE_CLASS_TEXT
                            or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                            or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                    .positiveText(R.string.submit)
                    .alwaysCallInputCallback() // this forces the callback to be invoked with every input change
                    .input(
                    R.string.input_hint,
                    0,
                    false
                    ) { dialog, input-> if (input.toString().equals("hello")) {
                        dialog.setContent("I told you not to type that!")
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false)
                    } else {
                        dialog.setContent(R.string.input_content_custominvalidation)
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true)
                    } }
                    .show()
        })

        input_checkPrompt.setOnClickListener(View.OnClickListener {
             MaterialDialog.Builder(this)
                     .title(R.string.input)
                     .content(R.string.input_content)
                     .inputType(
                             InputType.TYPE_CLASS_TEXT
                             or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                             or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                     .inputRange(2, 16)
                     .positiveText(R.string.submit)
                     .input(
                     R.string.input_hint,
                     R.string.input_hint,
                     false
                     ) { dialog, input-> showToast("Hello, " + input.toString() + "!") }
                        .checkBoxPromptRes(R.string.example_prompt, true, null)
                     .show()
        })

        progress1.setOnClickListener(View.OnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.progress_dialog)
                    .content(R.string.please_wait)
                    .contentGravity(GravityEnum.CENTER)
                    .progress(false, 150, true)
                    .cancelListener { dialog ->
                        if (thread != null) {
                            thread!!.interrupt()
                        }
                    }
                    .showListener { dialogInterface ->
                        val dialog = dialogInterface as MaterialDialog
                        startThread(
                                {
                                    while (dialog.currentProgress !== dialog.maxProgress && !Thread.currentThread().isInterrupted) {
                                        if (dialog.isCancelled) {
                                            break
                                        }
                                        try {
                                            Thread.sleep(50)
                                        } catch (e: InterruptedException) {
                                            break
                                        }

                                        dialog.incrementProgress(1)
                                    }
                                    runOnUiThread {
                                        thread = null
                                        dialog.setContent(getString(R.string.md_done_label))
                                    }
                                })
                    }
                    .show()
        })

        progress2.setOnClickListener(View.OnClickListener {
            showIndeterminateProgressDialog(false)
        })

        progress3.setOnClickListener(View.OnClickListener {
            showIndeterminateProgressDialog(true)
        })

        preference_dialogs.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))

        })

    }

    private fun showIndeterminateProgressDialog(horizontal: Boolean) {
        MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .show()
    }

    override fun onColorSelection(dialog: ColorChooserDialog, color: Int) {
        if (dialog.isAccentMode()) {
            accentPreselect = color
            ThemeSingleton.get().positiveColor = DialogUtils.getActionTextStateList(this, color)
            ThemeSingleton.get().neutralColor = DialogUtils.getActionTextStateList(this, color)
            ThemeSingleton.get().negativeColor = DialogUtils.getActionTextStateList(this, color)
            ThemeSingleton.get().widgetColor = color
        } else {
            primaryPreselect = color
            if (supportActionBar != null) {
                supportActionBar!!.setBackgroundDrawable(ColorDrawable(color))
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = CircleView.shiftColorDown(color)
                window.navigationBarColor = color
            }
        }
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog?) {
        showToast("Color chooser dismissed!")
    }

    override fun onFolderSelection(dialog: FolderChooserDialog, folder: File) {
        showToast(folder.getAbsolutePath())
    }

    override fun onFolderChooserDismissed(dialog: FolderChooserDialog?) {
        showToast("Folder chooser dismissed!")
    }

    override fun onFileSelection(dialog: FileChooserDialog?, file: File) {
        showToast(file.getAbsolutePath())
    }

    override fun onFileChooserDismissed(dialog: FileChooserDialog?) {
        showToast("File chooser dismissed!")
    }

    override fun onPause() {
        super.onPause()
        if (thread != null && !thread!!.isInterrupted() && thread!!.isAlive()) {
            thread!!.interrupt()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler = null
    }

    private fun showToast(message: String) {
        if (toast != null) {
            toast!!.cancel()
            toast = Toast(this)
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
//        toast.show()
    }

    private fun startThread(run: () -> Unit) {
        if (thread != null) {
            thread!!.interrupt()
        }
        thread = Thread(run)
        thread!!.start()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_RC) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handler!!.postDelayed({ findViewById<View>(chooserDialog).performClick() }, 1000)
            } else {
                Toast.makeText(
                        this,
                        "The folder or file chooser will not work without " + "permission to read external storage.",
                        Toast.LENGTH_LONG)
                        .show()
            }
        }
    }
}
