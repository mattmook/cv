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

struct SkillsView: View {
    var skills: [Skill_]

    var body: some View {
        FlexibleView(data: skills, spacing: 4, alignment: .leading) { skill in
            ChipView(text: skill.title)
        }.padding(.horizontal, 16)
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

struct SkillsView_Previews: PreviewProvider {
    static var previews: some View {
        ForEach(ColorScheme.allCases, id: \.self) {
            SkillsView(skills: [Skill_(skill: "Kotlin", since: 2017), Skill_(skill: "Swift", since: nil)])
                .preferredColorScheme($0)
        }
    }
}
