package com.mattdolan.cv.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.mattdolan.cv.androidApp.R
import com.mattdolan.cv.common.ui.component.Chip
import com.mattdolan.cv.common.ui.component.TwoLineWithIconAndMetaText
import com.mattdolan.cv.common.ui.component.TwoLineWithMetaText
import com.mattdolan.cv.common.ui.component.roboto
import com.mattdolan.cv.common.ui.component.robotoSlab
import com.mattdolan.cv.domain.Experience
import com.mattdolan.cv.domain.Role
import com.mattdolan.cv.experience.ErrorScreen
import com.mattdolan.cv.experience.LoadingScreen
import com.mattdolan.cv.profile.model.ProfileState
import com.mattdolan.cv.profile.model.ProfileViewModel
import java.time.Year

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    when (val state = viewModel.container.stateFlow.collectAsState().value) {
        ProfileState.Error -> ErrorScreen(
            errorMessage = stringResource(R.string.sorry),
            buttonTitle = stringResource(R.string.retry),
            buttonClick = { viewModel.loadProfile() })

        ProfileState.Loading -> LoadingScreen()

        is ProfileState.Ready -> ProfileReadyScreen(state) { role -> viewModel.selectRole(role) }
    }
}

@Composable
fun ProfileReadyScreen(state: ProfileState.Ready, selectRole: (Role) -> Unit) {
    val year = Year.now().value

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (headerBackground, image, name, tagline, location, chipGroup, experience) = createRefs()

        Box(
            modifier = Modifier
                .background(colorResource(R.color.chip_background))
                .constrainAs(headerBackground) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(chipGroup.bottom)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
        )

        Image(
            painter = rememberImagePainter(
                data = state.personalDetails.avatarUrl,
                builder = { transformations(CircleCropTransformation()) }
            ),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start, 16.dp)
                    //end.linkTo(parent.end)
                    top.linkTo(name.top)
                    bottom.linkTo(tagline.bottom)
                    height = Dimension.value(40.dp)
                    width = Dimension.value(40.dp)
                }
        )

        Text(
            state.personalDetails.name,
            modifier = Modifier
                //.padding(top = 16.dp)
                .constrainAs(name) {
                    //start.linkTo(image.end, 16.dp)
                    //end.linkTo(parent.end)
                    linkTo(start = image.end, end = parent.end, 16.dp, 0.dp, 0f)
                    top.linkTo(parent.top, 16.dp)
                    bottom.linkTo(tagline.top)
                },
            fontFamily = roboto,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )

        Text(
            state.personalDetails.tagline,
            modifier = Modifier
                .constrainAs(tagline) {
                    //start.linkTo(image.end, 16.dp)
                    //end.linkTo(parent.end)
                    linkTo(start = image.end, end = parent.end, 16.dp, 0.dp, 0f)
                    top.linkTo(name.bottom)
                    bottom.linkTo(location.top)
                },
            fontFamily = robotoSlab,
            fontSize = 14.sp
        )

        Row(
            modifier = Modifier
                .clipToBounds()
                .constrainAs(location) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(tagline.bottom)
                    bottom.linkTo(chipGroup.top)
                    height = Dimension.value(0.dp)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.map_marker),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .padding(end = 4.dp)
            )

            Text(
                state.personalDetails.location,
                fontFamily = robotoSlab,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp
            )
        }

        FlowRow(
            mainAxisSpacing = 4.dp,
            crossAxisSpacing = 4.dp,
            mainAxisAlignment = MainAxisAlignment.Center,
            modifier = Modifier
                .clipToBounds()
                .padding(16.dp)
                .constrainAs(chipGroup) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(location.bottom, 16.dp)
                    bottom.linkTo(experience.top)
                    height = Dimension.value(0.dp)
                }
        ) {
            state.skills.map { skill ->
                Chip(skill.since?.let { it -> "${skill.skill} (${year - it} yrs)" }
                    ?: skill.skill)
            }
        }

        Experiences(
            experiences = state.experiences,
            selectRole = selectRole,
            modifier = Modifier
                .constrainAs(experience) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(chipGroup.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
fun Experiences(
    experiences: List<Experience>,
    selectRole: (Role) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        experiences.forEachIndexed { companyIndex, experience ->
            item {
                TwoLineWithIconAndMetaText(
                    supportingVisual = rememberImagePainter(
                        data = experience.logoUrl,
                        builder = {
                            placeholder(R.drawable.ic_company_placeholder)
                        }
                    ),
                    primaryText = experience.company,
                    secondaryText = stringResource(
                        R.string.industry,
                        experience.industry,
                        experience.location
                    ),
                    metadata = experience.period,
                    topLine = companyIndex != 0,
                    bottomLine = true
                )
            }

            itemsIndexed(experience.roles) { roleIndex, role ->
                TwoLineWithMetaText(
                    primaryText = role.title,
                    secondaryText = role.team,
                    metadata = role.period,
                    topLine = true,
                    bottomLine = companyIndex != experiences.size - 1 || roleIndex != experience.roles.size - 1,
                    onClick = {
                        selectRole(role)
                    }
                )
            }
        }
    }
}
