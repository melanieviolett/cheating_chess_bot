import React from "react";

export default function CheatIndicator({ show })
{
    return(
        <div 
            className={`
                fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2
                bg-red-600 text-white p-4 rounded-lg z-50
                transition-opacity duration-500
                ${show ? 'opacity-100 animate-flash' : 'opacity-0'}
                font-bold
            `}
        >
            The AI has cheated and switched pieces with you!
        </div>
    );
}