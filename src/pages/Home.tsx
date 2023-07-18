import {IonContent, IonHeader, IonInput, IonPage, IonToolbar} from "@ionic/react";
import {useState} from "react";
import {useQuery} from "react-query";

import "./Home.css";
import {AuthApi} from "../api";

const Home = () => {
  const [username, setUsername] = useState<string>("");

  useQuery(["username"], async () => await AuthApi.username(username), {
    enabled: !!username,
    refetchOnMount: false,
    refetchOnReconnect: false,
    refetchOnWindowFocus: false,
  });

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar color="primary">
          <h2 style={{width: "100%", textAlign: "center"}}>JoyID</h2>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonInput
          label="Username"
          clearInput={true}
          labelPlacement="floating"
          placeholder="Input your username"
          onIonChange={(e) => setUsername(e.target.value!!.toString())}
        />
      </IonContent>
    </IonPage>
  );
};

export default Home;
