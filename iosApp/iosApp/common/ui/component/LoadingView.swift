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

struct LoadingView: View {

    @Environment(\.colorScheme) var colorScheme

    var body: some View {
        LottieView(name: colorScheme == .dark ? "cv-loading-dark" : "cv-loading")
            .frame(width: 128, height: 128)
            .id(colorScheme)
    }
}

struct LoadingView_Previews: PreviewProvider {
    static var previews: some View {
        ForEach(ColorScheme.allCases, id: \.self) {
            LoadingView().preferredColorScheme($0)
        }
    }
}
