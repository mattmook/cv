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

class RoleViewModel : ObservableObject {
    
    @Published var experience: Experience_
    @Published var role: Role_

    @Published var state: RoleState = RoleState()
    
    private let profileRepository = Sdk().profileRepository()
    
    init(experience: Experience_, role: Role_) {
        self.experience = experience
        self.role = role
        
        loadHeader()
        //loadDetails()
    }
    
    private func loadHeader() {
        state = state.copy(
            header: RoleState.Header(
                logoUrl: experience.logoUrl,
                title: role.title,
                team: role.team,
                period: role.period
            )
        )
    }
    
    func loadDetails() {
        state = state.copy(details: .Loading)
        
        roleDetails(role: role)
            .map({ (roleDetails) -> RoleState.Details in
                .Ready(roleDetails.description_)
            })
            .replaceError(with: .Error)
            .map { (roleDetails) -> RoleState in
                self.state.copy(details: roleDetails)
            }
            .assign(to: &$state)
    }
    
    private func roleDetails(role: Role_) -> AnyPublisher<RoleDetails_, Error> {
        return Deferred {
            Future<RoleDetails_, Error> { promise in
                self.profileRepository.roleDetails(role: role) { (roleDetails: RoleDetails_?, error: Error?) in
                    if let error = error {
                        promise(.failure(error))
                    } else {
                        promise(.success(roleDetails!))
                    }
                }
            }
        }.eraseToAnyPublisher()
    }
}
