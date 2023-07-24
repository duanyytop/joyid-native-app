// const randomHex = (size: number) => [...Array(size)].map(() => Math.floor(Math.random() * 16).toString(16)).join("");

export const generateRegistryParams = (_username: string): string => {
  return JSON.stringify({
    challenge: "5cz-1QcjEjzc-MnsRYLOmcXj-umEJO_1Sbj2lUNt2cQ",
    rp: {
      name: "JoyID App",
      id: "https://joyid.dev",
    },
    user: {
      id: "RqhH1qaRGL1VS2vLI-xHTOpra7Ycz6FtI35yhzuOEeE",
      name: "dylan",
      displayName: "dylan",
    },
    pubKeyCredParams: [
      {
        type: "public-key",
        alg: -7,
      },
      {
        type: "public-key",
        alg: -257,
      },
    ],
    timeout: 1800000,
    attestation: "none",
    excludeCredentials: [],
    authenticatorSelection: {
      authenticatorAttachment: "platform",
      userVerification: "required",
    },
    extensions: {
      credProps: true,
    },
  });
};
