import axios from "axios";
import {BaseResp, RegisterResp} from "./type";

const SessionIdKey = "connect.sid=";
const BaseUrl = "https://webauthn-codelab.glitch.me";

export class AuthApi {
  private static sessionId?: string;

  private static async baseCall({path, params, cookie}: {path: string; params?: object; cookie?: string}): Promise<BaseResp | undefined> {
    try {
      const response = await axios({
        method: "post",
        url: `${BaseUrl}/${path}`,
        headers: {
          "Content-Type": "application/json",
          "X-Requested-With": "XMLHttpRequest",
          Cookie: cookie,
        },
        timeout: 600000,
        data: JSON.stringify(params, null, ""),
      });
      const {data, headers} = response;
      this.sessionId = parseSessionId(headers["set-cookie"]);

      if (data) {
        console.error(response);
      } else {
        return data;
      }
    } catch (error) {
      console.error("error", error);
    }
  }

  static async username(name: string): Promise<BaseResp> {
    return (await this.baseCall({path: "username", params: {username: name}})) as Promise<BaseResp>;
  }

  static async registerRequest(): Promise<RegisterResp> {
    const params = {attestation: "none", authenticatorSelection: {authenticatorAttachment: "platform", userVerification: "required"}};
    return (await this.baseCall({path: "registerRequest", cookie: this.sessionId, params})) as Promise<RegisterResp>;
  }
}

const parseSessionId = (cookies: string[] | undefined): string | undefined => {
  const cookie = cookies?.find((item) => item.startsWith(SessionIdKey));
  if (cookie) {
    const start = cookie.indexOf(SessionIdKey);
    if (start < 0) {
      throw new Error(`Cannot find ${SessionIdKey}`);
    }
    const semicolon = cookie.indexOf(";", start + SessionIdKey.length);
    const end = semicolon < 0 ? cookie.length : semicolon;
    return cookie.substring(start + SessionIdKey.length, end);
  }
  return undefined;
};
