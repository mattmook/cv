//
// Copyright 2021 Matthew Dolan
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

import SwiftUI
import shared

struct PersonalDetailsView: View {
    var personalDetails: PersonalDetails_

    var body: some View {
        VStack {
            AsyncImage(
                url: URL(string: personalDetails.avatarUrl)!,
                placeholder: { Circle().foregroundColor(Color.tertiarySystemGroupedBackground) },
                image: { Image(uiImage: $0).resizable() }
            )
                .frame(width: 128, height: 128)
                .clipShape(Circle())

            Text(personalDetails.name).font(.system(.body))

            Text(personalDetails.tagline).font(.system(.caption))

            Text(personalDetails.location).font(.system(.caption))
        }.padding(.horizontal, 16)
    }
}

struct PersonalDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        ForEach(ColorScheme.allCases, id: \.self) {
            PersonalDetailsView(
                personalDetails: PersonalDetails_(
                    name: "Matthew Dolan",
                    tagline: "Experienced Mobile Software Specialist",
                    location: "London, UK",
                    avatarUrl: "https://s.gravatar.com/avatar/cc6ebb978003e63edda0ad8161cd04cd.jpeg?s=300"
                )
            ).preferredColorScheme($0)
        }
    }
}
