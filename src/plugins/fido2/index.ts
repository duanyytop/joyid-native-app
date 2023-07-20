import {registerPlugin} from "@capacitor/core";

import type {FidoPlugin} from "./definitions";

const Fido = registerPlugin<FidoPlugin>("Fido");

export * from "./definitions";
export {Fido};
