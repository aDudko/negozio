import './index.css';
import { Provider } from "react-redux";
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import ReactDOM from 'react-dom/client';
import React  from 'react';
import {AuthLayout} from "./layout/Auth/AuthLayout.tsx";
import {store} from "./store/store.ts";
import {RequireAuth} from "./helper/RequireAuth.tsx";
import {Layout} from "./layout/Layout/Layout.tsx";
import {Login} from "./pages/Login/Login.tsx";
import {Register} from "./pages/Register/Register.tsx";
import {Error as ErrorPage} from "./pages/Error/Error.tsx";

const router = createBrowserRouter([
    {
        path: '/',
        element: <RequireAuth><Layout /></RequireAuth>,
        children: [ ]
    },
    {
        path: '/auth',
        element: <AuthLayout />,
        children: [
            {
                path: 'login',
                element: <Login />
            }, {
                path: 'register',
                element: <Register />
            }
        ]
    },
    {
        path: '*',
        element: <ErrorPage />
    }
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <Provider store={store}>
            <RouterProvider router={router} />
        </Provider>
    </React.StrictMode>
);
