/*
 * Copyright 2021 Matthew Dolan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mattdolan.cv.experience

import android.view.View
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.kaspersky.kaspresso.screens.KScreen
import com.mattdolan.cv.androidApp.R
import org.hamcrest.Matcher

object RoleDetailScreen : KScreen<RoleDetailScreen>() {
    override val layoutId: Int = R.layout.role_detail_fragment
    override val viewClass: Class<*> = RoleDetailFragment::class.java

    val backButton = KButton { withId(R.id.back) }

    val errorImage = KImageView { withId(R.id.error) }

    val retryButton = KButton { withId(R.id.error_button) }

    val description: KRecyclerView = KRecyclerView({
        withId(R.id.content)
    }, itemTypeBuilder = {
        itemType(::DescriptionItem)
    })

    class DescriptionItem(parent: Matcher<View>) : KRecyclerItem<DescriptionItem>(parent) {
        val primaryText = KTextView(parent) { withId(R.id.primaryText) }
    }
}
