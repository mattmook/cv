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

struct ChipView: View {
    var text: String
    
    var body: some View {
        Text(text)
            .padding(4)
            .foregroundColor(Color.secondary)
            .background(RoundedRectangle(cornerRadius: CGFloat.infinity).stroke(Color.secondary))
            .font(.system(.caption2))
    }
}

struct ChipView_Previews: PreviewProvider {
    static var previews: some View {
        ForEach(ColorScheme.allCases, id: \.self) {
            ChipView(text: "Hello world")
                .preferredColorScheme($0)
        }

        
    }
}
