import java.util.*;

@FunctionalInterface
interface EligibilityCriteria<T> {
    boolean checkEligibility(T entity);
}

class Voter {
    private String voterId;
    private String name;
    private String address;
    private String dateOfBirth;
    private boolean registrationStatus;
    private String votedCandidateId;

    public Voter(String voterId, String name, String address, String dateOfBirth, boolean registrationStatus) {
        this.voterId = voterId;
        this.name = name;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.registrationStatus = registrationStatus;
    }

    public String getVoterId() {
        return voterId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean isRegistrationStatus() {
        return registrationStatus;
    }

    public String getVotedCandidateId() {
        return votedCandidateId;
    }

    public void setVotedCandidateId(String votedCandidateId) {
        this.votedCandidateId = votedCandidateId;
    }
}

class Candidate {
    private String candidateId;
    private String name;
    private String partyAffiliation;
    private int age;
    private int voteCount;

    public Candidate(String candidateId, String name, String partyAffiliation, int age) {
        this.candidateId = candidateId;
        this.name = name;
        this.partyAffiliation = partyAffiliation;
        this.age = age;
        this.voteCount = 0;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public String getName() {
        return name;
    }

    public String getPartyAffiliation() {
        return partyAffiliation;
    }

    public int getAge() {
        return age;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void incrementVoteCount() {
        this.voteCount++;
    }
}

class ElectionEntity {
    private String electionId;
    private String electionName;
    private String electionType;
    private String electionDate;
    private String electionStatus;

    public ElectionEntity(String electionId, String electionName, String electionType,
                          String electionDate, String electionStatus) {
        this.electionId = electionId;
        this.electionName = electionName;
        this.electionType = electionType;
        this.electionDate = electionDate;
        this.electionStatus = electionStatus;
    }

    public String getElectionId() {
        return electionId;
    }

    public String getElectionName() {
        return electionName;
    }

    public String getElectionType() {
        return electionType;
    }

    public String getElectionDate() {
        return electionDate;
    }

    public String getElectionStatus() {
        return electionStatus;
    }
}

public class ElectionSystem {
    private static List<Candidate> candidates = new ArrayList<>();
    private static List<Voter> voters = new ArrayList<>();
    private static ElectionEntity electionEntity;


    public static <T> void filterEntities(List<T> entities, EligibilityCriteria<T> criteria) {
        System.out.println("List of Eligible entities:");
        for (T entity : entities) {
            if (criteria.checkEligibility(entity)) {
                if (entity instanceof Candidate) {
                    System.out.println(((Candidate) entity).getName());
                } else if (entity instanceof Voter) {
                    System.out.println(((Voter) entity).getName());
                }
            }
        }
        System.out.println();
    }

 
    public static void castVote(Voter voter, Candidate candidate) {
        if (voter.isRegistrationStatus() && !hasVoterAlreadyVoted(voter)) {
            voter.setVotedCandidateId(candidate.getCandidateId());
            candidate.incrementVoteCount();
            System.out.println("Vote cast successfully by " + voter.getName() + " for " + candidate.getName() + ".");
        } else {
            System.out.println("Vote not cast. Either voter is not registered or has already voted.");
        }
    }

    
    private static boolean hasVoterAlreadyVoted(Voter voter) {
        return voter.getVotedCandidateId() != null;
    }

   
    public static void displayResults() {
        System.out.println("\nElection Results:");
        Candidate winner = findWinner();
        if (winner != null) {
            System.out.println("Winner: " + winner.getName() + " (Party: " + winner.getPartyAffiliation() + ")");
            System.out.println("Total Votes: " + voters.size());
            System.out.println(winner.getName() + "'s Vote Count: " + winner.getVoteCount());
        } else {
            System.out.println("No winner. No votes cast yet.");
        }
    }

    private static Candidate findWinner() {
        if (!candidates.isEmpty()) {
            return Collections.max(candidates, Comparator.comparing(Candidate::getVoteCount));
        }
        return null;
    }


    public static void inputElectionEntityDetails(Scanner scanner) {
        System.out.println("Enter details for Election Entity:");
        System.out.print("Election ID: ");
        String electionId = scanner.nextLine();
        System.out.print("Election Name: ");
        String electionName = scanner.nextLine();
        System.out.print("Election Type: ");
        String electionType = scanner.nextLine();
        System.out.print("Election Date: ");
        String electionDate = scanner.nextLine();
        System.out.print("Election Status: ");
        String electionStatus = scanner.nextLine();

        electionEntity = new ElectionEntity(electionId, electionName, electionType, electionDate, electionStatus);
    }

  
    public static void displayElectionEntityDetails() {
        if (electionEntity != null) {
            System.out.println("\nElection Entity Details:");
            System.out.println("Election ID: " + electionEntity.getElectionId());
            System.out.println("Election Name: " + electionEntity.getElectionName());
            System.out.println("Election Type: " + electionEntity.getElectionType());
            System.out.println("Election Date: " + electionEntity.getElectionDate());
            System.out.println("Election Status: " + electionEntity.getElectionStatus());
        } else {
            System.out.println("\nElection Entity details not available.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        inputElectionEntityDetails(scanner);

        System.out.print("\nEnter the number of candidates: ");
        int numberOfCandidates = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numberOfCandidates; i++) {
            System.out.println("Enter details for Candidate " + (i + 1) + ":");
            System.out.print("Candidate ID: ");
            String candidateId = scanner.nextLine();
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Party Affiliation: ");
            String partyAffiliation = scanner.nextLine();
            System.out.print("Age: ");
            int age = scanner.nextInt();
            scanner.nextLine();

           
            EligibilityCriteria<Candidate> eligibilityCriteria = c -> c.getAge() >= 25;

            if (eligibilityCriteria.checkEligibility(new Candidate(candidateId, name, partyAffiliation, age))) {
                Candidate candidate = new Candidate(candidateId, name, partyAffiliation, age);
                candidates.add(candidate);
            } else {
                System.out.println("Candidate not eligible (age < 25).");
            }
        }

        System.out.print("\nEnter the number of voters: ");
        int numberOfVoters = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numberOfVoters; i++) {
            System.out.println("Enter details for Voter " + (i + 1) + ":");
            System.out.print("Voter ID: ");
            String voterId = scanner.nextLine();
            System.out.print("Name: ");
            String voterName = scanner.nextLine();
            System.out.print("Address: ");
            String voterAddress = scanner.nextLine();
            System.out.print("Date of Birth: ");
            String voterDateOfBirth = scanner.nextLine();
            System.out.print("Voter Registration Status (true/false): ");
            boolean voterRegistrationStatus = scanner.nextBoolean();
            scanner.nextLine();

            Voter voter = new Voter(voterId, voterName, voterAddress, voterDateOfBirth, voterRegistrationStatus);
            voters.add(voter);
        }

        System.out.println("\nEligible Candidates:");

       
        filterEntities(candidates, candidate -> candidate.getAge() >= 25);

        System.out.println("\nEligible Voters:");
        filterEntities(voters, Voter::isRegistrationStatus);

        for (Voter voter : voters) {
            if (voter.isRegistrationStatus()) {
                System.out.println("\nVoter: " + voter.getName() + " (ID: " + voter.getVoterId() + ")");
                System.out.println("Available Candidates:");
                filterEntities(candidates, candidate -> true);
                System.out.print("Enter the Candidate ID you want to vote for: ");
                String votedCandidateId = scanner.nextLine();
                Candidate votedCandidate = candidates.stream()
                        .filter(candidate -> votedCandidateId.equals(candidate.getCandidateId()))
                        .findFirst()
                        .orElse(null);
                if (votedCandidate != null) {
                    castVote(voter, votedCandidate);
                } else {
                    System.out.println("Invalid Candidate ID. Vote not cast.");
                }
            }
        }

        displayResults();
        displayElectionEntityDetails();

        scanner.close();
    }
}
