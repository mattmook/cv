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
import Combine
import shared

struct ProfileView: View {
    
    @StateObject private var profileViewModel = ProfileViewModel()
    @State var play = 0
    
    var body: some View {
        NavigationView {
            switch profileViewModel.state {
            case .Ready (let (personalDetails, experiences, skills)):
                List {
                    VStack {
                        AsyncImage(
                            url: URL(string: personalDetails.avatarUrl)!,
                            placeholder: { Text("Loading ...") },
                            image: { Image(uiImage: $0).resizable() }
                        )
                        .frame(width: 128, height: 128)
                        .clipShape(Circle())
                        
                        Text(personalDetails.name).font(.system(.body))
                        
                        Text(personalDetails.tagline).font(.system(.caption))
                        
                        Text(personalDetails.location).font(.system(.caption))
                        
                        FlexibleView(data: skills, spacing: 4, alignment: .leading) { skill in
                            ChipView(text: skill.title)
                        }
                        
                    }.padding(16)
                    .background(Color.systemGroupedBackground)
                    .listRowInsets(EdgeInsets())
                    
                    ForEach(experiences, id: \.hash) { experience in
                        Section(header:
                                    TwoLineWithMetaTextView(primaryText: experience.company, secondaryText: "\(experience.industry) • \(experience.location)", metadata: experience.period)
                                    .padding(.vertical, 8)
                        ) {
                            ForEach(experience.roles, id: \.hash) { role in
                                TwoLineWithMetaTextView(primaryText: role.title, secondaryText: role.team, metadata: role.period)
                            }
                        }
                    }
                }.listStyle(GroupedListStyle())
                .navigationBarHidden(true)
            case .Error:
                ErrorView {
                    profileViewModel.loadProfile()
                }.navigationBarHidden(true)
            case .Loading:
                LoadingView()
                    .navigationBarHidden(true)
            }
        }
    }
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileView()
    }
}

private let year = Calendar.current.component(.year, from: Date())

private extension Skill_ {
    
    var title: String {
        if let since = since?.intValue {
            return "\(skill) (\(year - since) yrs)"
        } else {
            return skill
        }
    }
}
