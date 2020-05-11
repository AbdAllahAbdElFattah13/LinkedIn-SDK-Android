package com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder

import android.app.Activity
import android.content.Intent

class LinkedInFromActivityBuilder private constructor(private val activity: Activity) : LinkedInBuilder(activity) {
    override fun self(): LinkedInBuilder {
        return this
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        activity.startActivityForResult(intent, requestCode)
    }

    companion object {
        @JvmStatic
        fun getInstance(activity: Activity): LinkedInFromActivityBuilder {
            return LinkedInFromActivityBuilder(activity)
        }
    }

}