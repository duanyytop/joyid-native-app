import type {PluginListenerHandle} from "@capacitor/core";

export interface FidoPlugin {
  /**
   * Register fido to get credential.
   */
  register(opts: {params: string}): Promise<void>;

  /**
   * Listens for screen orientation changes.
   */
  addListener(
    eventName: "credentialValid",
    listenerFunc: (credential: string) => void
  ): Promise<PluginListenerHandle> & PluginListenerHandle;

  /**
   * Removes all listeners
   */
  removeAllListeners(): Promise<void>;
}
