import SwiftUI
import shared

class AModel: ObservableObject, Kotlinx_coroutines_coreFlowCollector {
  @Published var email = ""
    
    func emit(value: Any?, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        print("ios received " + (value as! String))

        email = value as! String
    }
}

func greet() -> String {
    return "old school"
}

struct ContentView: View {

    @ObservedObject
    var state: AModel = AModel()
    
    var greeting: Greeting = Greeting()
    
    init() {
       greeting.greeting().collect(collector: state) { (result: KotlinUnit?, error: Error?) in
            print("ios completion")
        }
        
        print("should collect block?")
    }
    
    var body: some View {
        VStack {
            Text(state.email)
            
            Button(action: {
                greeting.loadSkills()
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
