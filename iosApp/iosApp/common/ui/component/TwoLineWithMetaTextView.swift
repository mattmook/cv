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

struct TwoLineWithMetaTextView: View {
    var primaryText: String
    var secondaryText: String?
    var metadata: String
    var action: (() -> Any)? = nil
    
    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                HStack(alignment: .firstTextBaseline) {
                    Text(primaryText).foregroundColor(Color.primary).font(.system(.body))
                    Spacer()
                    Text(metadata).foregroundColor(Color.primary).font(.system(.caption))
                }
                if let secondaryText = secondaryText {
                    Text(secondaryText).foregroundColor(Color.secondary).font(.system(.caption))
                }
            }
            if action != nil {
                Image(systemName: "chevron.right")
                    .foregroundColor(Color.tertiaryLabel)
                    .padding(.leading, 8)
            }
        }.frame(minHeight: 28) // 28 + 8 + 8 = 44
        .padding(EdgeInsets(top: 8, leading: 16, bottom: 8, trailing: 16))
        
    }
}

struct TwoLineWithMetaTextView_Previews: PreviewProvider {
    static var previews: some View {
        ForEach(ColorScheme.allCases, id: \.self) {
            TwoLineWithMetaTextView(primaryText: "Babylon Health", secondaryText: "Health care • London", metadata: "07/2018 - present")
                .preferredColorScheme($0)
        }
    }
}
