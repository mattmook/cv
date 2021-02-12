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

struct ExperiencesView: View {
    var experiences: [Experience_]
    
    var body: some View {
        VStack(spacing: 0) {
            ForEach(experiences, id: \.hash) { experience in
                TwoLineWithMetaTextView(primaryText: experience.company, secondaryText: "\(experience.industry) • \(experience.location)", metadata: experience.period)
                    .padding(.top, 16)
                
                Divider()
                
                ForEach(experience.roles.indices) { index in
                    let role = experience.roles[index]
                    
                    if (index != 0) {
                        Divider().padding(.leading, 16).background(Color.secondarySystemGroupedBackground)
                    }
                    
                    TwoLineWithMetaTextView(primaryText: role.title, secondaryText: role.team, metadata: role.period) {
                        // navigate here
                    }
                    .background(Color.secondarySystemGroupedBackground)
                }
                Divider()
            }
        }.background(Color.systemGroupedBackground)
    }
}

struct ExperiencesView_Previews: PreviewProvider {
    static var previews: some View {
        ForEach(ColorScheme.allCases, id: \.self) {
            ExperiencesView(
                experiences: [
                    Experience_(
                        company: "Babylon Health",
                        logoUrl: "",
                        industry: "Health care",
                        location: "London",
                        period: "07/2018 - present",
                        roles: [
                            Role_(
                                title: "Senior Android Engineer",
                                team: "Android platform team",
                                period: "07/2018 - present",
                                detailUrl: ""),
                            Role_(
                                title: "Senior Android Engineer",
                                team: "Android platform team",
                                period: "07/2018 - present",
                                detailUrl: ""),
                            Role_(
                                title: "Senior Android Engineer",
                                team: "Android platform team",
                                period: "07/2018 - present",
                                detailUrl: "")
                        ]
                    ),
                    Experience_(
                        company: "Babylon Health",
                        logoUrl: "",
                        industry: "Health care",
                        location: "London",
                        period: "07/2018 - present",
                        roles: [
                            Role_(
                                title: "Senior Android Engineer",
                                team: "Android platform team",
                                period: "07/2018 - present",
                                detailUrl: "")
                        ]
                    )

                ]
            )
            .preferredColorScheme($0)
        }
    }
}
