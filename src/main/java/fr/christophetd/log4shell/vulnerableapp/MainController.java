package fr.christophetd.log4shell.vulnerableapp;


import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
public class MainController {

    private static final Logger logger = LogManager.getLogger("HelloWorld");

    @GetMapping("/")
    public String index(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if (!auth.matches("[a-zA-Z0-9 =]*")){
            logger.error("wrong auth header");
            auth = "";
        }
        logger.info("Asking for index");
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <script>\n" +
                "        function sendRequestLog4ShellHeader(){\n" +
                "            var xmlhttp = new XMLHttpRequest();\n" +
                "            xmlhttp.open(\"GET\", \"vulnerable_header/\");\n" +
                "            xmlhttp.setRequestHeader(\"Authorization\", \"" + auth + "\");\n" +
                "            xmlhttp.setRequestHeader(\"Accept\", \"text/html,application/xhtml+xml\");\n" +
                "            xmlhttp.setRequestHeader(\"X-Api-Version\", \"the_vulnerable_header\");\n" +
                "            xmlhttp.send(\"\");\n" +
                "        }\n" +
                "        function sendRequestLog4ShellParam(form){\n" +
                "            var example = form.example.value;\n" +
                "            var xmlhttp = new XMLHttpRequest();\n" +
                "            xmlhttp.open(\"GET\", \"vulnerable_param/?example=\" + example);\n" +
                "            xmlhttp.setRequestHeader(\"Authorization\", \"Basic cHJvYmVseTpzd2Vhcl9wbGFpbl9tYW51YWw=\");\n" +
                "            xmlhttp.setRequestHeader(\"Accept\", \"text/html,application/xhtml+xml\");\n" +
                "            xmlhttp.send(\"\");\n" +
                "        }\n" +
                "        function sendRequestLog4ShellBody(form){\n" +
                "            var example = form.example.value;\n" +
                "            var xmlhttp = new XMLHttpRequest();\n" +
                "            xmlhttp.open(\"POST\", \"vulnerable_body/\");\n" +
                "            xmlhttp.setRequestHeader(\"Authorization\", \"" + auth + "\");\n" +
                "            xmlhttp.setRequestHeader(\"Accept\", \"text/html,application/xhtml+xml\");\n" +
                "            xmlhttp.send(example);\n" +
                "        }\n" +
                "        function sendRequestLog4ShellBodyParam(form){\n" +
                "            var username = form.user.value;\n" +
                "            var password = form.pass.value;\n" +
                "            var xmlhttp = new XMLHttpRequest();\n" +
                "            xmlhttp.open(\"POST\", \"vulnerable_body_param/\");\n" +
                "            xmlhttp.setRequestHeader(\"Authorization\", \"" + auth + "\");\n" +
                "            xmlhttp.setRequestHeader(\"Accept\", \"text/html,application/xhtml+xml\");\n" +
                "            xmlhttp.setRequestHeader(\"Content-Type\", \"application/json\");\n" +
                "            xmlhttp.send(\"{\\\"user\\\": \\\"\" + username + \"\\\", \\\"pass\\\": \\\"\" + password + \"\\\"}\");\n" +
                "        }\n" +
                "    </script>\n" +
                "</head>\n" +
                "    <body>\n" +
                "    <p> Execute: </p>\n" +
                "    <form name=\"form\" action=\"\" method=\"GET\">\n" +
                "        Request with custom Header: <br>\n" +
                "        <input type=\"button\" name=\"button\" value=\"Click\" onClick=\"sendRequestLog4ShellHeader()\">\n" +
                "    </form>\n" +
                "    <form name=\"form\" action=\"\" method=\"GET\">\n" +
                "        Request with param: <br>\n" +
                "        <input type=\"text\" name=\"example\" value=\"example\"> <br>\n" +
                "        <input type=\"button\" name=\"button\" value=\"Click\" onClick=\"sendRequestLog4ShellParam(this.form)\">\n" +
                "    </form>\n" +
                "    <form name=\"form\" action=\"\" method=\"GET\">\n" +
                "        Request with body: <br>\n" +
                "        <input type=\"text\" name=\"example\" value=\"example\"> <br>\n" +
                "        <input type=\"button\" name=\"button\" value=\"Click\" onClick=\"sendRequestLog4ShellBody(this.form)\">\n" +
                "    </form>\n" +
                "    <form name=\"form\" action=\"\" method=\"GET\">\n" +
                "        Request with body param: <br>\n" +
                "        <input type=\"text\" name=\"user\" value=\"user1\"> <br>\n" +
                "        <input type=\"text\" name=\"pass\" value=\"password\"> <br>" +
                "        <input type=\"button\" name=\"button\" value=\"Click\" onClick=\"sendRequestLog4ShellBodyParam(this.form)\">\n" +
                "    </form>\n" +
                "    </body></html>";
    }

    @GetMapping("/vulnerable_header/")
    public String vulnerableHeader(@RequestHeader("X-Api-Version") String apiVersion) {
        logger.info("Received a request for API version " + apiVersion);
        return "Hello, world!";
    }

    @GetMapping("/vulnerable_param/")
    public String vulnerableParam(@RequestParam("example") String example) {
        logger.info("Received a request with param example: " + example);
        return "Hello, world!";
    }

    @PostMapping("/vulnerable_body/")
    public String vulnerableBody(@RequestBody String exampleBody) {
        logger.info("Received a request with body: " + exampleBody);
        return "Hello, world!";
    }

    @PostMapping("/vulnerable_body_param/")
    public String vulnerableBodyParam(@RequestBody LoginExample loginExample) {
        logger.info("Received a request with login user: " + loginExample.getUser());
        logger.info("Received a request with login pass: " + loginExample.getPass());
        return "Hello, world!";
    }

}