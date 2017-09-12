@RestController
class WebApp {
    @RequestMapping("/")
    String greetings() {
        "스프링 부트 시작"
    }
}
