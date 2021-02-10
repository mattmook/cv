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

class ProfileViewModel : ObservableObject {
    
    @Published var state: ProfileState = .Loading
    
    private let profileRepository = Sdk().profileRepository()
    
    init() {
        loadProfile()
    }
    
    func loadProfile() {
        state = .Loading
        Publishers.CombineLatest3(personalDetails(), experiences(), skills()).map { (personalDetails, experiences, skills) -> ProfileState in
            .Ready((personalDetails, experiences, skills))
        }.replaceError(with: .Error)
        .eraseToAnyPublisher()
        .assign(to: &$state)
    }
    
    private func personalDetails() -> AnyPublisher<PersonalDetails_, Error> {
        return Deferred {
            Future<PersonalDetails_, Error> { promise in
                self.profileRepository.personalDetails { (personalDetails: PersonalDetails_?, error: Error?) in
                    if let error = error {
                        promise(.failure(error))
                    } else {
                        promise(.success(personalDetails!))
                    }
                }
            }
        }.eraseToAnyPublisher()
    }
    
    private func experiences() -> AnyPublisher<[Experience_], Error> {
        return Deferred {
            Future<[Experience_], Error> { promise in
                self.profileRepository.experiences { (experiences: [Experience_]?, error: Error?) in
                    if let error = error {
                        promise(.failure(error))
                    } else {
                        promise(.success(experiences!))
                    }
                }
            }
        }.eraseToAnyPublisher()
    }

    private func skills() -> AnyPublisher<[Skill_], Error> {
        return Deferred {
            Future<[Skill_], Error> { promise in
                self.profileRepository.skills { (skills: [Skill_]?, error: Error?) in
                    if let error = error {
                        promise(.failure(error))
                    } else {
                        promise(.success(skills!))
                    }
                }
            }
        }.eraseToAnyPublisher()
    }
}
