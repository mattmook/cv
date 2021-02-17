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
import SDWebImageSwiftUI

public struct RoleView: View {

    @StateObject var roleViewModel: RoleViewModel

    public var body: some View {
        let logoUrlPdf = URL(string: roleViewModel.state.header.logoUrl.replacingOccurrences(of: ".svg", with: ".pdf"))!

        switch roleViewModel.state.details {
        case .Ready(let details):
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    ForEach(details, id: \.self) { detail in
                        HStack(alignment: .firstTextBaseline) {
                            Text("•")
                            Text(detail).font(.body).foregroundColor(.primary)
                        }.padding(8)
                    }
                }
            }
                .navigationBarTitleDisplayMode(.inline)
                .toolbar {
                    ToolbarView(primaryText: roleViewModel.state.header.title, secondaryText: roleViewModel.state.header.team, logoUrl: logoUrlPdf)
                }
        case .Error:
            ErrorView {
                roleViewModel.loadDetails()
            }
                .navigationBarTitle(roleViewModel.state.header.title, displayMode: .inline)
                .toolbar {
                    ToolbarView(primaryText: roleViewModel.state.header.title, secondaryText: roleViewModel.state.header.team, logoUrl: logoUrlPdf)
                }
        case .Loading:
            LoadingView()
                .navigationBarTitle(roleViewModel.state.header.title, displayMode: .inline)
                .toolbar {
                    ToolbarView(primaryText: roleViewModel.state.header.title, secondaryText: roleViewModel.state.header.team, logoUrl: logoUrlPdf)
                }
        }
    }
}

private struct ToolbarView: ToolbarContent {
    var primaryText: String
    var secondaryText: String?
    var logoUrl: URL

    var body: some ToolbarContent {
        ToolbarItem(placement: .principal) {
            HStack {
                AnimatedImage(url: logoUrl)
                    .resizable()
                    .frame(width: 32, height: 32)

                VStack(alignment: .leading) {
                    Text(primaryText).font(.headline).foregroundColor(Color.primary)

                    if let secondaryText = secondaryText {
                        Text(secondaryText).font(.caption).foregroundColor(Color.secondary)
                    }
                }
            }
        }
    }
}

struct RoleView_Previews: PreviewProvider {
    static var previews: some View {

        let role = Role_(
            title: "Senior Android Engineer",
            team: "Android platform team",
            period: "07/2018 - present",
            detailUrl: "https://somewhere"
        )

        RoleView.create(
            experience: Experience_(
                company: "Babylon Health",
                logoUrl: "",
                industry: "Health care",
                location: "London",
                period: "07/2018 - present",
                roles: [role]),
            role: role
        )
    }
}

extension RoleView {
    static func create(experience: Experience_, role: Role_) -> some View {
        RoleView(roleViewModel: .init(experience: experience, role: role))
    }
}
