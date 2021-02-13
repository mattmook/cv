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

import shared

struct RoleState {
    let header: Header?
    let details: Details
    
    init() {
        self.header = nil
        self.details = .Loading
    }
    
    init(header: Header?, details: Details) {
        self.header = header
        self.details = details
    }
    
    func copy(header: Header?) -> RoleState {
        return RoleState(header: header, details: self.details)
    }
    
    func copy(details: Details) -> RoleState {
        return RoleState(header: self.header, details: details)
    }
    
    struct Header {
        let logoUrl: String
        let title: String
        let team: String?
        let period: String
    }
    
    enum Details {
        case Loading
        case Error
        case Ready([String])
    }
}
