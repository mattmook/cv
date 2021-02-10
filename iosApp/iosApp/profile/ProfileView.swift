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
    
    
    
    var body: some View {
        NavigationView {
            switch profileViewModel.state {
            case .Ready (let (personalDetails, experiences, skills)):
                ScrollView {
                    VStack {
                        Text(personalDetails.name)
                            .navigationBarHidden(true)
                    
                        FlexibleView(data: skills, spacing: 4, alignment: .leading) { skill in
                            ChipView(text: skill.title)
                        }
                        .padding(8)
                    }.background(Color(hex: 0xebebeb))
                    
                    ForEach(0..<experiences.count) { index in
                        Text(experiences[index].company)
                    }
                }
            case .Error:
                Text("Error")
                    .navigationBarHidden(true)
            case .Loading:
                Text("Loading")
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
