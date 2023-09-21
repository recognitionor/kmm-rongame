import SwiftUI
import shared

struct ContentView: View {
	

	var body: some View {
        VStack {
            ComposeView().padding(.top, 30) // 예를 들어 20만큼의 패딩
        }.edgesIgnoringSafeArea(.all)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
