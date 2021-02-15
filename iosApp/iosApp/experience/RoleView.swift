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

public struct RoleView: View {
    
    @StateObject var roleViewModel: RoleViewModel
    
    public var body: some View {
        Text(roleViewModel.state.header?.title ?? "n/a")
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
