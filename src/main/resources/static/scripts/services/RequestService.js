

class RequestService {

    static processResponse(response) {
        if (response.status >= 500) {
            alert("Internal server error!");
            throw new Error("Internal server error! status=" + response.status);
        }
    }

    static processResponseAndDoRedirect(response, redirectUrl) {
        this.processResponse(response);
        this.doRedirect(redirectUrl);
    }

    static doRedirect(redirectUrl) {
        console.log(redirectUrl);
        window.location.replace(redirectUrl);
    }
}
