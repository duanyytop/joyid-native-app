import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'io.nervina.joyid',
  appName: 'joyid',
  webDir: 'dist',
  server: {
    androidScheme: 'https'
  }
};

export default config;
