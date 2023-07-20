import React, {useMemo} from "react";
import ReactDOM from "react-dom/client";
import {Router} from "./Router.tsx";
import "./index.css";
import {QueryClient, QueryClientProvider} from "react-query";
import {ChakraProvider} from "@chakra-ui/react";

const App: React.FC = () => {
  const queryClient = useMemo(() => new QueryClient(), []);
  return (
    <QueryClientProvider client={queryClient}>
      <ChakraProvider>
        <Router />
      </ChakraProvider>
    </QueryClientProvider>
  );
};

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
