import {RouteObject, RouterProvider, createBrowserRouter} from "react-router-dom";
import {setupIonicReact} from "@ionic/react";
import Home from "./pages/Home";

setupIonicReact();

const routers: RouteObject[] = [
  {
    path: "/",
    element: <Home />,
  },
];
const router = createBrowserRouter(routers);

export const Router = () => <RouterProvider router={router} />;
