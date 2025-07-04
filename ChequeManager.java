package com.wahab.myapp;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

@SuppressWarnings("rawtypes")
public class ChequeManager extends Contract {
    public static final String BINARY = "YOUR_COMPILED_CONTRACT_BYTECODE_HERE";

    public static final String ABI = "[\n" +
            "\t{\n" +
            "\t\t\"anonymous\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"chequeId\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"ChequeCancelled\",\n" +
            "\t\t\"type\": \"event\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"anonymous\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"chequeId\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"payer\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"beneficiary\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"chequeType\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"ChequeIssued\",\n" +
            "\t\t\"type\": \"event\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"chequeId\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"cancelCheque\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"cheques\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"payer\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"beneficiary\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"chequeType\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"bool\",\n" +
            "\t\t\t\t\"name\": \"isCancelled\",\n" +
            "\t\t\t\t\"type\": \"bool\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"chequeId\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"isChequeValid\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"bool\",\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"bool\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"chequeId\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"payer\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"beneficiary\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"internalType\": \"string\",\n" +
            "\t\t\t\t\"name\": \"chequeType\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"issueCheque\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t}\n" +
            "]"; // Keep your full ABI here (truncated for brevity)

    protected ChequeManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(ABI, contractAddress, web3j, transactionManager, gasProvider);
    }

    // ✅ Matches ABI
    public RemoteCall<TransactionReceipt> issueCheque(String chequeId, String payer, String beneficiary, String chequeType) {
        final Function function = new Function(
                "issueCheque",
                Arrays.<Type>asList(new Utf8String(chequeId), new Utf8String(payer), new Utf8String(beneficiary), new Utf8String(chequeType)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    // ✅ Matches ABI
    public RemoteCall<TransactionReceipt> cancelCheque(String chequeId) {
        final Function function = new Function(
                "cancelCheque",
                Arrays.<Type>asList(new Utf8String(chequeId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    // ✅ Matches ABI
    public RemoteCall<Boolean> isChequeValid(String chequeId) {
        final Function function = new Function(
                "isChequeValid",
                Arrays.<Type>asList(new Utf8String(chequeId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static RemoteCall<ChequeManager> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ChequeManager.class, web3j, transactionManager, contractGasProvider, BINARY, ABI);
    }

    public static ChequeManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ChequeManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }
}
