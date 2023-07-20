import {Button, Input, useToast} from "@chakra-ui/react";
import {useState} from "react";

import "./Home.css";
import {generateRegistryParams} from "../webauthn";
import {Fido} from "../plugins/fido2";

const Home = () => {
  const toast = useToast();
  const [username, setUsername] = useState<string>("");

  const register = async () => {
    if (!username) {
      toast({
        title: "Username cannot be empty",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
    const params = generateRegistryParams(username);
    Fido.addListener("credentialValid", (credential) => {
      if (credential === "error") {
        toast({
          title: "Register fail",
          status: "error",
          duration: 3000,
          isClosable: true,
        });
      } else {
        console.log(JSON.stringify(credential));
      }
    });
    await Fido.register({params});
  };

  return (
    <div className="content">
      <Input placeholder="Input username" width="280px" onChange={(e) => setUsername(e.target.value!!.toString())} />
      <Button colorScheme="blue" marginTop="60px" width="120px" onClick={register}>
        Register
      </Button>
    </div>
  );
};

export default Home;
