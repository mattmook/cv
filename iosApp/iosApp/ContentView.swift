import SwiftUI
import shared

struct ContentView: View {

    var body: some View {
        VStack {
            Text("hello")
            
            Button(action: {
                
            }) {
                Text("Button title")
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
