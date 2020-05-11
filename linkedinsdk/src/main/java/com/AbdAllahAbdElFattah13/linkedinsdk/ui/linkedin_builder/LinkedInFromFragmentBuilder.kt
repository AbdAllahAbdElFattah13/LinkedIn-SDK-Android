package com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder

import android.content.Intent
import androidx.fragment.app.Fragment

class LinkedInFromFragmentBuilder private constructor(private val fragment: Fragment) : LinkedInBuilder(fragment.activity) {
    override fun self(): LinkedInBuilder {
        return this
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        fragment.startActivityForResult(intent, requestCode)
    }

    companion object {
        fun getInstance(fragment: Fragment): LinkedInFromFragmentBuilder {
            return LinkedInFromFragmentBuilder(fragment)
        }
    }
}